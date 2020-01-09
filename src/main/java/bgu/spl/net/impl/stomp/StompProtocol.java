package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.*;

import java.util.HashMap;

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate=false;
    private Connectionsimpl<String> connections;
    private int id;
    private StompFrame response;
    private HashMap<String,String> message;

    public void start(int connectionId, Connections<String> connections){
      this.id=connectionId;
      this.connections= (Connectionsimpl<String>)connections;
      message=new HashMap<String, String>();
    }

    public void process(StompFrame frame){
        frame.getType();
    }

    public boolean shouldTerminate(){
       return terminate;
    }


}
