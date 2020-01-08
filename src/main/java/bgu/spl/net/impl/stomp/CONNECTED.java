package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class CONNECTED extends StompFrame {

    private HashMap<String,String> map;

    public CONNECTED(String name, HashMap<String, String> message) {
        super(name, message);
        this.map=message;
    }

    @Override
    public String getType(){
        return "CONNECTED";
    }

    @Override
    public HashMap getHashMap(){
        return map;
    }
}
