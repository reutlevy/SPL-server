package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.*;

import java.util.HashMap;

public class StompProtocol implements StompMessagingProtocol<StompFrame> {

    private Boolean terminate=false;
    private Connectionsimpl<StompFrame> connections;
    private int id;
    private StompFrame response;
    private HashMap<String,String> message;

    @Override
    public void start(int connectionId, Connections<StompFrame> connections){
      this.id=connectionId;
      this.connections= connections;
      message=new HashMap<String, String>();
    }

    @Override
    public void process(StompFrame frame){
        frame.getType();
    }

    public boolean shouldTerminate(){
       return terminate;
    }


}
