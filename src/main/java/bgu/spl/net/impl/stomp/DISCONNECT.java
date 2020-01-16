package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

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

    public String toString(){

        String answer="DISCONNECTED";
        Iterator it = message.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            answer=answer+" "+pair.getKey() + ":" + pair.getValue();
            //   System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return answer;
    }
}
