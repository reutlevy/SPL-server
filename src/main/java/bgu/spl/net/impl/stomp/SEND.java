package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class SEND extends StompFrame {

    private String destination;
    private String body;
    private HashMap<String,String> message;
    private Boolean isError;

    public SEND(String name, HashMap<String, String> message) {
        super(name, message);
        this.message=message;

        this.body = message.getOrDefault("body", "");
        this.destination=message.get("destination");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "SEND";
    }

    @Override
    public HashMap getHashMap(){
        return message;
    }

    @Override
    public Boolean getIsError(){
        return isError;
    }
}
