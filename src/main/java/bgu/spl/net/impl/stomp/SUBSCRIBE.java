package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class SUBSCRIBE extends StompFrame {

    private String destination;
    private String id;
    private String body;
    private HashMap<String,String> message;

    public SUBSCRIBE(String name, HashMap<String,String> message){
        super(name,message);
        this.destination=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
        this.message=message;
    }

    @Override
    public String getType(){
        return "SUBSCRIBE";
    }

    @Override
    public HashMap getHashMap(){
        return message;
    }
}
