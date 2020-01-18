package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StompFrame {

    private String type;
    protected ConcurrentHashMap<String, String> headers;

    public StompFrame(String type1,ConcurrentHashMap<String, String> headers1){

        this.type=type1;
        this.headers = new ConcurrentHashMap<>();
        for(String key : headers1.keySet())
        {
            this.headers.put(key, headers1.get(key));
           // System.out.println(key + "   ddddddddddd   " + headers1.get(key));
        }
    }

    public void setHeaders(ConcurrentHashMap<String, String> headers1)
    {
        this.headers = new ConcurrentHashMap<>();
        for(String key : headers1.keySet())
        {
            this.headers.put(key, headers1.get(key));
        }
//        headers=new ConcurrentHashMap<>();
//        this.headers = headers1;
    }

    public String getType() {
        return type;
    }

    public ConcurrentHashMap<String, String> getHashMap() {
        for (String s : headers.keySet()) {
            System.out.println("THE KEY IS" + s);
            System.out.println("THE VALUE IS" + headers.get(s));

        }
        return headers;
    }

   /* public String toString() {

        String answer = this.type;
        System.out.println("the headersssss are"+ headers);
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry) it.next();
            answer = answer + " " + pair.getKey() + ":" + pair.getValue() + "\n";
            //   System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        answer = answer.replace("body", "\n");
        if (!headers.containsKey("body"))
            answer += "\n";

        return answer;
    } */

    @Override
    public String toString() {
        String output = this.type + "\n";
        for (String key : headers.keySet()) {
            output += key + ":" + headers.get(key) + "\n";
        }
       // output = '\n' + '\u0000';
        return output;
    }
}
