package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class UNSUBSCRIBE extends StompFrame {

    private String id;
    private HashMap<String,String> message;

    public UNSUBSCRIBE(String name, HashMap<String,String> message){
        super(name,message);
        this.id=message.get("id");
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
