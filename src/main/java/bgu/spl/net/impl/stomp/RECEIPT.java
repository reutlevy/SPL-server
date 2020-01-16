package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

public class RECEIPT extends StompFrame {

    private String receiptid;
    private HashMap<String,String> message;
    private Boolean isError;

    public RECEIPT(String name, HashMap<String, String> message) {
        super(name, message);
        this.message=message;

        this.receiptid=message.get("receipt-id");
        this.isError=false;
    }

    @Override
    public String getType(){
        return "RECEIPT";
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

        String answer="RECEIPT";
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
