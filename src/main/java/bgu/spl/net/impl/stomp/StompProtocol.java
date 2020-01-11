package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.net.srv.BookClubManager;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
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
    private String counterOfMsgId;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections) {
        this.id = connectionId;
        this.connections = (Connectionsimpl<StompFrame>) connections;
        message = new HashMap<String, String>();
        db = BookClubManager.getInstance();
        db.initialConnections((Connectionsimpl<StompFrame>) connections);
        counterOfMsgId = "";
    }

    public int getId() {
        return id;
    }

    @Override
    public void process(StompFrame frame) {

        String type = frame.getType();
        HashMap<String, String> FrameMap = frame.getHashMap();
        BookClubManager bm = BookClubManager.getInstance();
        counterOfMsgId += 1;

        if (type.equals("CONNECT")) {
            Boolean userNameExist = false;
            String userName = (String) FrameMap.get("login");
            String password = (String) FrameMap.get("passcode");
            HashMap<Integer, User> existUsersMap = bm.getExistusers();
            for (Integer i : existUsersMap.keySet()) {
                for (User u : existUsersMap.values()) {
                    while (!userNameExist) {
                        if (u.getUserName().equals(userName)) { //the user name is exist
                            userNameExist = true;
                            if (!u.getConnect() & u.getPassword().equals(password)) {  //password ok and not logged in
                                message.put("version", FrameMap.get("accept-version"));
                                response = new CONNECTED("CONNECTED", message);
                                connections.send(id, response);
                            } else if (!u.getConnect()) { //logged in already
                                message.put("message", "User alresdy logged in");
                                String msgToError = frame.getType() +"-";
                                message.put("receipt-id", msgToError+FrameMap.get("receipt"));
                                response = new ERROR("ERROR", message);
                                connections.send(id, response);
                            } else if (!u.getPassword().equals(password)) { //password wrong
                                message.put("message", "Wrong password");
                                String msgToError = frame.getType() +"-";
                                message.put("receipt-id",  msgToError+FrameMap.get("receipt"));
                                response = new ERROR("ERROR", message);
                                connections.send(id, response);
                            }
                        }
                    }
                }
            }
            if (!userNameExist) { //create new user (user not exist)
                LinkedList<String> genres = new LinkedList<String>();
                HashMap<String,String> inventory = new HashMap<>();
                User newUser = new User(userName, password, true, genres, id,inventory );
                existUsersMap.put(id, newUser);
                bm.getLogedinusers().put(id, newUser);
            }
        }

        if (type.equals("DISCONNECT")) {
            LinkedList<String> genres = ((User)bm.getExistusers().get(id)).getGenres();
            for(String s: genres){
                ((LinkedList<Integer>)bm.getgenres().get(s)).remove(id);
            }
            terminate = true;
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
            connections.send(id, response);
        }

        if (type.equals("SUBSCRIBE")) {
            if(!bm.getgenres().containsKey(FrameMap.get("destination"))){
                LinkedList<Integer> newList = new LinkedList<>();
                newList.add(id);
                bm.getgenres().put(FrameMap.get("destination"), newList);
            }
            else ((LinkedList<Integer>)bm.getgenres().get("destination")).add(id);
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
            connections.send(id, response);
        }

        if (type.equals("SEND")) {
             if (FrameMap.get("body").contains("added")) {
                 String username = (FrameMap.get("body").split(" "))[0];
                 String bookname = (FrameMap.get("body").split(" "))[5];
                 HashMap<Integer, User> existUsersMap = bm.getExistusers();
                 for (Integer i : existUsersMap.keySet()) {
                     for (User u : existUsersMap.values()) {
                         if (u.getUserName().equals(username))
                             u.getInventory().put(bookname, FrameMap.get("destination"));
                     }
                 }
             }

             if (FrameMap.get("body").contains("borrow")){
                 message.put("subscription", String.valueOf(id));
                 message.put("Message-id", counterOfMsgId);
                 message.put("destination", FrameMap.get("destination"));
                 message.put("body", FrameMap.get("body"));
                 response = new MESSAGE("MESSAGE", message);
                 connections.send(FrameMap.get("destination"),response);
                 //TODO Check if its message or send
            /*     String WishedBook = (FrameMap.get("body").split(" "))[4];//searching if someone have this book in their inventory
                 HashMap<Integer, User> existUsersMap = bm.getExistusers();
                 Boolean found = false;
                 for (Integer i : existUsersMap.keySet()) {
                     for (User u : existUsersMap.values()) {
                         while (!found){
                             if(u.getInventory().containsKey(WishedBook)){
                                 found=true;
                                 String username = u.getUserName();
                                 counterOfMsgId += 1;
                                 message.put("subscription", String.valueOf(id));
                                 message.put("Message-id", counterOfMsgId);
                                 message.put("destination", FrameMap.get("destination"));
                                 message.put("body", username+" has "+ WishedBook);
                                 response = new MESSAGE("MESSAGE", message);
                                 connections.send(FrameMap.get("destination"),response);
                             }
                         }
                     }
                 }
          */ }

             if (FrameMap.get("body").contains("Taking")) {
                 String WishedBook = (FrameMap.get("body").split(" "))[1];
                 String Lender = (FrameMap.get("body").split(" "))[3];
                 User borrower = (User) bm.getExistusers().get(id);
                 borrower.getInventory().put(WishedBook, FrameMap.get("destination"));
                 HashMap<Integer, User> existUsersMap = bm.getExistusers();
                 for (Integer i : existUsersMap.keySet()) {
                     for (User u : existUsersMap.values()) {
                         if (u.getUserName().equals(Lender)) {
                             u.getInventory().remove(WishedBook);
                         }
                     }
                 }
                 message.put("subscription", String.valueOf(id));
                 message.put("Message-id", counterOfMsgId);
                 message.put("destination", FrameMap.get("destination"));
                 message.put("body", FrameMap.get("body"));
                 response = new MESSAGE("MESSAGE", message);
                 connections.send(FrameMap.get("destination"),response);
             }

             if (FrameMap.get("body").contains("Returning")) {
                 String returnto = (FrameMap.get("body").split(" "))[3];
                 String bookname = (FrameMap.get("body").split(" "))[1];
                 ((User) bm.getExistusers().get(id)).getInventory().remove(bookname);
                 HashMap<Integer, User> existUsersMap = bm.getExistusers();
                 for (Integer i : existUsersMap.keySet()) {
                     for (User u : existUsersMap.values()) {
                         if (u.getUserName().equals(returnto))
                             u.getInventory().put(bookname, FrameMap.get("destination"));

                     }
                 }
                 message.put("subscription", String.valueOf(id));
                 message.put("Message-id", counterOfMsgId);
                 message.put("destination", FrameMap.get("destination"));
                 message.put("body", FrameMap.get("body"));
                 response = new MESSAGE("MESSAGE", message);
                 connections.send(FrameMap.get("destination"), response);
             }

             if (FrameMap.get("body").contains("status")) {
                 message.put("subscription", String.valueOf(id));
                 message.put("Message-id", counterOfMsgId);
                 message.put("destination", FrameMap.get("destination"));
                 message.put("body", FrameMap.get("body"));
                 response = new MESSAGE("MESSAGE", message);
                 connections.send(FrameMap.get("destination"), response);
             }

             if (FrameMap.get("body").contains(":")) { //TODO if the Message need to be send after all the clients finish
                 message.put("subscription", String.valueOf(id));
                 message.put("Message-id", counterOfMsgId);
                 message.put("destination", FrameMap.get("destination"));
                 message.put("body", FrameMap.get("body"));
                 response = new MESSAGE("MESSAGE", message);
                 connections.send(FrameMap.get("destination"), response);
             }
        }

        if (type.equals("UNSUBSCRIBE")) {
            LinkedList<Integer> list = (LinkedList<Integer>) bm.getgenres().get(FrameMap.get("destination"));
            list.remove(id);
            message.put("receipt-id", FrameMap.get("receipt"));
            response = new RECEIPT("RECEIPT", message);
            connections.send(id, response);
        }

    }

    @Override
    public boolean shouldTerminate () {
        return terminate;
    }
}

