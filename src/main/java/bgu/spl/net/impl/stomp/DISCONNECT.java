package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class DISCONNECT extends StompFrame {

    private String receipt;
    private HashMap<String,String> message;
    private Boolean isError;

    public DISCONNECT(String name, HashMap<String, String> message) {
        super(name, message);
        this.message=message;

        this.receipt=message.get("receipt");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "DISCONNECT";
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
