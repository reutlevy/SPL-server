package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.StompFrame;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase<T> {

    private Connectionsimpl<StompFrame> connections = null;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<StompFrame>> Genres;
    private LinkedList<User> users;
    private HashMap<Integer, ConnectionHandler<T>> clients;

    private DataBase(){
       Genres=new ConcurrentHashMap<>();
       users=new LinkedList<>();
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
}
