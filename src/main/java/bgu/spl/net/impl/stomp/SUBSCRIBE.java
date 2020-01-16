package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

public class SUBSCRIBE extends StompFrame {

    private String destination;
    private String id;
    private String receipt;
    private HashMap<String, String> message;
    private Boolean isError;

    public SUBSCRIBE(String name, HashMap<String, String> message) {
        super(name, message);
        this.message = message;

        this.destination = message.get("destination");
        this.id = message.get("id");
        this.receipt = message.get("receipt");
        this.isError = false;
    }

    @Override
    public String getType() {
        return "SUBSCRIBE";
    }

    @Override
    public HashMap getHashMap() {
        return message;
    }

    @Override
    public Boolean getIsError() {
        return isError;
    }

    public String toString() {

        String answer = "SUBSCRIBE";
        Iterator it = message.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            answer = answer + " " + pair.getKey() + ":" + pair.getValue();
            //   System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return answer;
    }
}