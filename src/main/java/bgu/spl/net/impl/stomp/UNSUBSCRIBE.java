package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class UNSUBSCRIBE extends StompFrame {

    private String id;
    private HashMap<String,String> message;
    private Boolean isError;

    public UNSUBSCRIBE(String name, HashMap<String,String> message){
        super(name,message);
        this.message=message;

        this.id=message.get("id");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "SUBSCRIBE";
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
