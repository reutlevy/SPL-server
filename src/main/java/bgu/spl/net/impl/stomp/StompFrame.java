package bgu.spl.net.impl.stomp;

import java.util.HashMap;

abstract class StompFrame {

    private String type;
    private HashMap messages;

    public StompFrame(String type, HashMap<String,String> message){
        this.type=type;
        this.messages=message;
    }

    abstract public String getType();

    abstract public HashMap getHashMap();
}
