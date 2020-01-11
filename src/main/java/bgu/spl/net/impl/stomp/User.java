package bgu.spl.net.impl.stomp;

import java.util.HashMap;
import java.util.LinkedList;

public class User {

    private String userName;
    private String password;
    private Boolean isConnect;
    private LinkedList<String> genres;
    private HashMap<String,String> inventory;
    private Integer connectionId;

    public User(String userName, String password, Boolean isConnect, LinkedList<String> genres, Integer connectionId, HashMap<String,String> inventory){
        this.userName=userName;
        this.password=password;
        this.isConnect=isConnect;
        this.genres=genres;
        this.connectionId=connectionId;
        this.inventory=inventory;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getConnect() {
        return isConnect;
    }

    public LinkedList<String> getGenres() {
        return genres;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public HashMap<String, String> getInventory() {
        return inventory;
    }
}
