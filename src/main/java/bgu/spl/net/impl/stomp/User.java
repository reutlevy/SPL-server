package bgu.spl.net.impl.stomp;

import java.awt.*;

public class User {

    private String userName;
    private int password;
    private Boolean isConnect;
    private List genres = new List();
    private Integer connectionId;

    public User(String userName, int password, Boolean isConnect, List genres, Integer connectionId){
        this.userName=userName;
        this.password=password;
        this.isConnect=isConnect;
        this.genres=genres;
        this.connectionId=connectionId;
    }

    public String getUserName() {
        return userName;
    }

    public int getPassword() {
        return password;
    }

    public Boolean getConnect() {
        return isConnect;
    }

    public List getGenres() {
        return genres;
    }

    public Integer getConnectionId() {
        return connectionId;
    }
}
