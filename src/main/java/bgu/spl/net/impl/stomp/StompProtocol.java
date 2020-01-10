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

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate=false;
    private Connectionsimpl<StompFrame> connections;
    private int id;
    private StompFrame response;
    private HashMap<String,String> message;
    private BookClubManager db;
    private String counterOfI;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections){
      this.id=connectionId;
      this.connections= (Connectionsimpl<StompFrame>)connections;
      message=new HashMap<String, String>();
      db= BookClubManager.getInstance();
      db.initialConnections((Connectionsimpl<StompFrame>) connections);
      counterOfI="";
    }

    @Override
    public void process(StompFrame frame){

        String type=frame.getType();
        HashMap<String,String> FrameMap = frame.getHashMap();
        BookClubManager bm = BookClubManager.getInstance();
        counterOfI += 1;

        if(type.equals("CONNECT")){
            //TODO checking socket
           if( checking socket is ok){
                Boolean userNameExist = false;
                String userName = (String) FrameMap.get("login");
                String password = (String) FrameMap.get("passcode");
                HashMap<Integer,User> existUsersMap =bm.getExistusers();
                for(Integer i : existUsersMap.keySet()) {
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
                                    message.put("receipt-id", counterOfI);
                                    response = new ERROR("ERROR", message);
                                    connections.send(id, response);
                                } else if (!u.getPassword().equals(password)) { //password wrong
                                    message.put("message", "Wrong password");
                                    message.put("receipt-id", counterOfI);
                                    response = new ERROR("ERROR", message);
                                    connections.send(id, response);
                                }
                            }
                        }
                    }
                }
                if (!userNameExist) { //create new user (user not exist)
                    LinkedList<String> genres = new LinkedList<String>();
                    User newUser = new User(userName, password, true, genres, id);
                    existUsersMap.put(id, newUser);
                    bm.getLogedinusers().put(id, newUser);
                    //TODO the client need to print "Login succesful"
                }
        }else{
               //TODO to check if we need send frame or just message
               HashMap<String,String> message = new HashMap<>();
               message.put("message", "Could not connect to server");
               message.put("receipt-id", counterOfI);
               response = new ERROR("ERROR",message);
               connections.send(id,response);
           }
        }


        if(type.equals("DISCONNECT")){
            terminate=true;
            message.put("receipt-id",add.get("receipt"));
            response=new RECEIPT("RECEIPT",message);
            connections.send(id,response);
        }

        if(type.equals("SUBSCRIBE")){
            message.put("receipt-id",add.get("receipt"));
            response=new RECEIPT("RECEIPT",message);
            connections.send(id,response);
        }
        if((type.equals("MESSAGE")||(type.equals("SEND")))){
            message.put("subscription",String.valueOf(id));
            message.put("destination",add.get("destination"));
            message.put("body",add.get("body"));
            response=new MESSAGE("MESSAGE",message);
            connections.send(id,response);
        }
        if(frame.getIsError()){
            message.put("receipt-id",add.get("receipt"));
            response=new ERROR("RECEIPT",message);
            connections.send(id,response);
        }
    }

    public boolean shouldTerminate(){
       return terminate;
    }


}
