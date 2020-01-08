package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class MESSAGE extends StompFrame {

    private String subscription;
    private String MessageId;
    private String destination;
    private String body;
    private HashMap<String,String> message;

    public MESSAGE(String name, HashMap<String, String> message) {
        super(name, message);
        this.subscription=message.get("subscription");
        this.MessageId=message.get("Message-id");
        this.destination=message.get("destination");
        this.body = message.getOrDefault("body", "");
        this.message=message;
    }

    @Override
    public String getType(){
        return "MESSAGE";
    }

    @Override
    public HashMap getHashMap(){
        return message;
    }
}