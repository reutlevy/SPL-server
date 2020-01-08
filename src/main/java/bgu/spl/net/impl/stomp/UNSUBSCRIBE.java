package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class UNSUBSCRIBE extends StompFrame {

    private String id;

    public UNSUBSCRIBE(String name, HashMap<String,String> message){
        super(name,message);
        this.id=message.get("id");
    }
}
