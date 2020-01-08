package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class SEND extends StompFrame {

    private String destination;
    private String body;

    public SEND(String name, HashMap<String, String> message) {
        super(name, message);
        this.body = message.getOrDefault("body", "");
        this.destination=message.get("destination");
    }
}
