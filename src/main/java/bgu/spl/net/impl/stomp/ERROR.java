package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

public class ERROR extends StompFrame {

    private String receiptid;
    private String body;
    private String message;
    private HashMap<String,String> messages;
    private Boolean isError;

    public ERROR(String name, HashMap<String, String> message) {
        super(name, message);
        this.receiptid=message.get("receipt-id");
        this.body = message.getOrDefault("body", "");
        this.message=message.get("message");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "ERROR";
    }

    @Override
    public HashMap getHashMap(){
        return messages;
    }

    @Override
    public Boolean getIsError(){
        return isError;
    }

    public String toString(){

        String answer="ERROR";
        Iterator it = messages.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            answer=answer+" "+pair.getKey() + ":" + pair.getValue();
            //   System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return answer;
    }
}
