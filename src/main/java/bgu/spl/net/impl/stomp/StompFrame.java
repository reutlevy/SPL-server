package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public abstract class StompFrame {

    private String type;

    public StompFrame(String type, HashMap<String,String> message){
        this.type=type;
    }
}
