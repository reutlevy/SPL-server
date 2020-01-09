package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.net.srv.DataBase;

import java.util.HashMap;

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate=false;
    private Connectionsimpl<StompFrame> connections;
    private int id;
    private StompFrame response;
    private HashMap<String,String> message;
    private DataBase db;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections){
      this.id=connectionId;
      this.connections= (Connectionsimpl<StompFrame>)connections;
      message=new HashMap<String, String>();
      db=DataBase.getInstance();
      db.initialConnections((Connectionsimpl<StompFrame>) connections);
    }

    @Override
    public void process(StompFrame frame){
        String type=frame.getType();
        HashMap<String,String> add=frame.getHashMap();
        if(type.equals("DISCONNECT")){
            terminate=true;
            message.put("receipt-id",add.get("receipt"));
            response=new RECEIPT("RECEIPT",message);
            connections.send(id,response);
        }
        if(type.equals("CONNECT")){
            message.put("version",add.get("accept-version"));
            response=new CONNECTED("CONNECTED",message);
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
