package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class CONNECTED extends StompFrame {

    private HashMap<String,String> map;
    private Boolean isError;
    private String version;

    public CONNECTED(String name, HashMap<String, String> message) {
        super(name, message);
        this.map=message;
        this.version=message.get("version");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "CONNECTED";
    }

    @Override
    public HashMap getHashMap(){
        return map;
    }

    @Override
    public Boolean getIsError(){
        return isError;
    }
}
