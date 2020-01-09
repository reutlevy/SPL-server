package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.BookClubManager;
import bgu.spl.net.srv.Connectionsimpl;
import bgu.spl.net.srv.DataBase;

import java.util.HashMap;

public class CONNECT extends StompFrame {

    private String acceptversion;
    private String host;
    private String login;
    private String passcode;
    private HashMap<String,String> map;
    private String body;
    private Boolean isError;

    public CONNECT(String name, HashMap<String, String> message) {
        super(name, message);
        this.body = message.getOrDefault("body", "");
        this.acceptversion=message.get("accept-version");
        this.host=message.get("host");
        this.login=message.get("login");
        this.passcode=message.get("passcode");
        this.isError=false;
    }
    public void process(StompFrame frame, Connectionsimpl<StompFrame> connections){
        BookClubManager bm = BookClubManager.getInstance();
        connections.




        HashMap mapToInsert = new HashMap()map.put("version",acceptversion));
        response=new CONNECTED("CONNECTED",message);
        connections.send(id,response);
    }
    @Override
    public String getType(){
        return "CONNECT";
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
