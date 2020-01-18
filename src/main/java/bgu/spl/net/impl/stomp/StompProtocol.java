package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.Frames.CONNECTED;
import bgu.spl.net.impl.stomp.Frames.ERROR;
import bgu.spl.net.impl.stomp.Frames.MESSAGE;
import bgu.spl.net.impl.stomp.Frames.RECEIPT;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.net.srv.BookClubManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate = false;
    private Connectionsimpl<StompFrame> connections;
    private int id;
    private StompFrame response;
    private BookClubManager db;
    private int counterOfMsgId;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections) {
        this.id = connectionId;
        this.connections = (Connectionsimpl<StompFrame>) connections;
        db = BookClubManager.getInstance();
        db.initialConnections((Connectionsimpl<StompFrame>) connections);
        counterOfMsgId = 0;
    }

    @Override
    public void process(StompFrame frame) {
        System.out.println("Protocol is process");
        String type = frame.getType();
        ConcurrentHashMap<String, String> FrameMap = frame.getHashMap();
        System.out.println(frame.getHashMap());
        BookClubManager bm = BookClubManager.getInstance();
        counterOfMsgId ++;


        if (type.equals("CONNECT")) {
            System.out.println("-------------------recognize CONNECTED" +
                   "-------------------");
            Boolean userNameExist = false;
            String userName = FrameMap.get("login");
         //   System.out.println("the user name issss "+ FrameMap.get("login"));
            String password = FrameMap.get("passcode");
         //   System.out.println("the user password issss "+FrameMap.get("passcode"));
            ConcurrentHashMap<Integer, User> existUsersMap = bm.getExistusers();
            for (User u : existUsersMap.values()) {
                while (!userNameExist) {
                    if (u.getUserName().equals(userName)) { //the user name is exist
                        userNameExist = true;
                        if (u.getConnect()) { //logged in already
                            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
                            message.put("message", "User alresdy logged in");
                            String msgToError = frame.getType() + "-";
                            message.put("receipt-id", msgToError + FrameMap.get("receipt"));
                            response = new ERROR(message);
                            response.setHeaders(message);
                            connections.send(id, response);
                        }
                        else if (!u.getPassword().equals(password)) { //password wrong
                            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
                            message.put("message", "Wrong password");
                            String msgToError = frame.getType() + "-";
                            message.put("receipt-id", msgToError + FrameMap.get("receipt"));
                            response = new ERROR(message);
                            response.setHeaders(message);
                            connections.send(id, response);
                        }
                        else{ //password ok and not logged in
                            int oldId = u.getConnectionId();
                            System.out.println("we are connected");
                            User dupU = u;
                            u.setConnect(true);
                            u.setConnectionId(id);
                            bm.getExistusers().remove(oldId);
                            bm.getExistusers().put(id,dupU);
                            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
                            message.put("version", FrameMap.get("accept-version"));
                            response = new CONNECTED(message);
                            response.setHeaders(message);
                            connections.send(id, response);
                        }
                    }
                }
            }
            if (!userNameExist) { //create new user (user not exist)
                ConcurrentHashMap<String,String> genres = new ConcurrentHashMap<>();
                User newUser = new User(userName, password, true, genres, id);
                existUsersMap.put(id, newUser);
                //newUser.setConnect(true);
                //System.out.println("12345677890" + newUser.getUserName());
                ConcurrentHashMap<String, String> outHeaders = new ConcurrentHashMap<>();
                outHeaders.put("accept-version", FrameMap.get("accept-version"));
                System.out.println(FrameMap.get("accept-version"));
                System.out.println("the out headers are----"+outHeaders);
                StompFrame response1=new CONNECTED(outHeaders);
                //response1.setHeaders(outHeaders);
                System.out.println("the response isssssssssss "+ response1);
            //    System.out.println("the headers offffff----"+response1.getHashMap().get("accept-version"));
                connections.send(id, response1);
            }
        }

        if (type.equals("DISCONNECT")) {
            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT(message);
            connections.send(id, response);
            terminate = true;
        }

        if (type.equals("SUBSCRIBE")) {
            if(!bm.getgenres().containsKey(FrameMap.get("destination"))){
                LinkedList<Integer> newList = new LinkedList<>();
                newList.add(id);
                bm.getgenres().put(FrameMap.get("destination"), newList);
                ((User)bm.getExistusers().get(id)).getGenres().put(FrameMap.get("id"),FrameMap.get("destination"));
            }
            else {
                ((LinkedList<Integer>)bm.getgenres().get("destination")).add(id);
            }
            ((User)bm.getExistusers().get(id)).getGenres().put(FrameMap.get("id"),FrameMap.get("destination"));

            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT(message);
            connections.send(id, response);
        }

        if (type.equals("SEND")) {
            counterOfMsgId++;
            String destination = FrameMap.get("destination");
            String body = FrameMap.get("body");
            response = this.getMessage(destination, body, String.valueOf(counterOfMsgId));
            connections.send(FrameMap.get("destination"), response);
        }

        if (type.equals("UNSUBSCRIBE")) {
            //TODO if genreToDelete null
            String genresToDeleteFrom = ((User)bm.getExistusers().get(id)).getGenres().get(FrameMap.get(id));
            ((User)bm.getExistusers().get(id)).getGenres().remove(FrameMap.get("id"));
            ((LinkedList<Integer>)bm.getgenres().get(genresToDeleteFrom)).remove(id);
            ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT(message);
            connections.send(id, response);
        }

    }

    @Override
    public boolean shouldTerminate () {
        return terminate;
    }

    public StompFrame getMessage(String destination, String body, String messageid){
        BookClubManager bm = BookClubManager.getInstance();
        int subscription=0;
        ConcurrentHashMap<String,String> listOfGenres = ((User)bm.getExistusers().get(id)).getGenres();
        for(String key : listOfGenres.keySet()){
            if(listOfGenres.get(key).equals(destination))
                subscription = Integer.parseInt(key);
        }
        ConcurrentHashMap<String,String> message=new ConcurrentHashMap<>();
        message.put("subscription", String.valueOf(subscription));
        message.put("destination", destination);
        message.put("body", body);
        message.put("Message-id", messageid);
        return new MESSAGE(message);
    }
}

