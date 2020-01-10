package bgu.spl.net.impl.stomp;

import java.util.LinkedList;

public class User {

    private String userName;
    private String password;
    private Boolean isConnect;
    private LinkedList<String> genres;
    private Integer connectionId;

    public User(String userName, String password, Boolean isConnect, LinkedList<String> genres, Integer connectionId){
        this.userName=userName;
        this.password=password;
        this.isConnect=isConnect;
        this.genres=genres;
        this.connectionId=connectionId;
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
}
