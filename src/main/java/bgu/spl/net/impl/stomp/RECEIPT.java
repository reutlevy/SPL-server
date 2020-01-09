package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class RECEIPT extends StompFrame {

    private String receiptid;
    private String body;
    private HashMap<String,String> message;
    private Boolean isError;

    public RECEIPT(String name, HashMap<String, String> message) {
        super(name, message);
        this.receiptid=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
        this.message=message;
        this.isError=false;
    }

    @Override
    public String getType(){
        return "RECEIPT";
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
