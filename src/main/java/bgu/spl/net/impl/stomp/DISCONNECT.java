package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class DISCONNECT extends StompFrame {

    private String receipt;
    private HashMap<String,String> message;

    public DISCONNECT(String name, HashMap<String, String> message) {
        super(name, message);
        this.receipt=message.get("receipt");
        this.message=message;
    }

    @Override
    public String getType(){
        return "DISCONNECT";
    }

    @Override
    public HashMap getHashMap(){
        return message;
    }
}
