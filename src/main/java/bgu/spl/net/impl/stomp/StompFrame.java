package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

public abstract class StompFrame {

    private String type;
    private HashMap messages;

    public StompFrame(String type, HashMap<String,String> message){
        this.type=type;
        this.messages=message;
    }

    public String getType(){
        return type;
    }

    public HashMap getHashMap(){
        return messages;
    }

    public Boolean getIsError(){
        return false;
    }

    public String toString(){

        String answer=type;
        Iterator it = messages.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            answer=answer+" "+pair.getKey() + ":" + pair.getValue();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return answer;
    }
}
