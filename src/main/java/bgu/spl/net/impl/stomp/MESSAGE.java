package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class MESSAGE extends StompFrame {

    private String subscription;
    private String MessageId;
    private String destination;
    private String body;

    public MESSAGE(String name, HashMap<String, String> message) {
        super(name, message);
        this.subscription=message.get("subscription");
        this.MessageId=message.get("Message-id");
        this.destination=message.get("destination");
        this.body = message.getOrDefault("body", "");
    }
}