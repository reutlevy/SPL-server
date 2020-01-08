package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class ERROR extends StompFrame {

    private String receiptid;
    private String body;
    private String message;

    public ERROR(String name, HashMap<String, String> message) {
        super(name, message);
        this.receiptid=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
        this.message=message.get("message");
    }
}
