package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class RECEIPT extends StompFrame {

    private String receiptid;
    private String body;
    private HashMap<String,String> message;

    public RECEIPT(String name, HashMap<String, String> message) {
        super(name, message);
        this.receiptid=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
        this.message=message;
    }

    @Override
    public String getType(){
        return "RECEIPT";
    }

    @Override
    public HashMap getHashMap(){
        return message;
    }
}
