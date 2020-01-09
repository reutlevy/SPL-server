package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.StompFrame;
import bgu.spl.net.impl.stomp.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase<T> {

    private Connectionsimpl<StompFrame> connections = null;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<StompFrame>> Genres;
    private LinkedList<User> loginusers;
    private LinkedList<User> activeusers;
    private HashMap<Integer, ConnectionHandler<T>> clients;

    private DataBase(){
       Genres=new ConcurrentHashMap<>();
        loginusers=new LinkedList<>();
        activeusers=new LinkedList<>();
       clients=new HashMap<>();
    }

    private static class Holder {
        private static DataBase dataBase = new DataBase();
    }
    public static DataBase getInstance() {
        return Holder.dataBase;
    }

    public void initialConnections(Connectionsimpl<StompFrame> connections) {
        if (this.connections == null)
            this.connections = connections;
    }

    public boolean checklogin(User user){
        return false;
    }

    public boolean checkexist(User user){
        return false;
    }

   
}
