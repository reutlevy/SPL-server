package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class RECEIPT extends StompFrame {

    private String receiptid;
    private String body;

    public RECEIPT(String name, HashMap<String, String> message) {
        super(name, message);
        this.receiptid=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
    }
}
