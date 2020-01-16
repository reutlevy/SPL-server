package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.Iterator;

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

    public String toString(){

        String answer="CONNECTED";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            answer=answer+" "+pair.getKey() + ":" + pair.getValue();
            //   System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return answer;
    }
}
