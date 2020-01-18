package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.net.srv.BookClubManager;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate = false;
    private Connectionsimpl<StompFrame> connections;
    private int id;
    private StompFrame response;
    private HashMap<String, String> message;
    private BookClubManager db;
    private int counterOfMsgId;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections) {
        this.id = connectionId;
        this.connections = (Connectionsimpl<StompFrame>) connections;
        message = new HashMap<String, String>();
        db = BookClubManager.getInstance();
        db.initialConnections((Connectionsimpl<StompFrame>) connections);
        counterOfMsgId = 0;
    }

    @Override
    public void process(StompFrame frame) {

        String type = frame.getType();
        HashMap<String, String> FrameMap = frame.getHashMap();
        BookClubManager bm = BookClubManager.getInstance();
        counterOfMsgId ++;


        if (type.equals("CONNECT")) {
            Boolean userNameExist = false;
            String userName = (String) FrameMap.get("login");
            String password = (String) FrameMap.get("passcode");
            HashMap<Integer, User> existUsersMap = bm.getExistusers();
            for (User u : existUsersMap.values()) {
                while (!userNameExist) {
                    if (u.getUserName().equals(userName)) { //the user name is exist
                        userNameExist = true;
                        if (u.getConnect()) { //logged in already
                            message.put("message", "User alresdy logged in");
                            String msgToError = frame.getType() + "-";
                            message.put("receipt-id", msgToError +"-"+ FrameMap.get("receipt"));
                            response = new ERROR("ERROR", message);
                            connections.send(id, response);
                        }
                        else if (!u.getPassword().equals(password)) { //password wrong
                            message.put("message", "Wrong password");
                            String msgToError = frame.getType() + "-";
                            message.put("receipt-id", msgToError + "-"+FrameMap.get("receipt"));
                            response = new ERROR("ERROR", message);
                            connections.send(id, response);
                        }
                        else{ //password ok and not logged in
                            int oldId = u.getConnectionId();
                            User dupU = u;
                            u.setConnect(true);
                            u.setConnectionId(id);
                            bm.getExistusers().remove(oldId);
                            bm.getExistusers().put(id,dupU);
                            message.put("version", FrameMap.get("accept-version"));
                            response = new CONNECTED("CONNECTED", message);
                            connections.send(id, response);
                        }
                    }
                }
            }
            if (!userNameExist) { //create new user (user not exist)
                HashMap<String,String> genres = new HashMap<>();
                User newUser = new User(userName, password, true, genres, id);
                existUsersMap.put(id, newUser);
                newUser.setConnect(true);
            }
        }

        if (type.equals("DISCONNECT")) {
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
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
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
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
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
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
        HashMap<String,String> listOfGenres = ((User)bm.getExistusers().get(id)).getGenres();
        for(String key : listOfGenres.keySet()){
            if(listOfGenres.get(key).equals(destination))
                subscription = Integer.parseInt(key);
        }
        message.put("subscription", String.valueOf(subscription));
        message.put("destination", destination);
        message.put("body", body);
        message.put("Message-id", messageid);
        return new MESSAGE("MESSAGE", message);
    }
}

