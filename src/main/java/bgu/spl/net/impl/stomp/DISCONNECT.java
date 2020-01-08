package bgu.spl.net.impl.stomp;

import java.util.HashMap;

public class DISCONNECT extends StompFrame {

    private String receipt;

    public DISCONNECT(String name, HashMap<String, String> message) {
        super(name, message);
        this.receipt=message.get("receipt");
    }
}
