package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public abstract class StompFrame {

    private String type;
    private HashMap messages;

    public StompFrame(String type, HashMap<String,String> message){
        this.type=type;
        this.messages=message;
    }

    public String getType(){
        return type;
    }

    public HashMap getHashMap(){
        return messages;
    }
}
