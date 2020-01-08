package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompProtocol implements StompMessagingProtocol {

    private Boolean terminate=false;

    public void start(int connectionId, Connections<String> connections){

    }

    public void process(String message){

    }

    public boolean shouldTerminate(){
       return terminate;
    }
}
