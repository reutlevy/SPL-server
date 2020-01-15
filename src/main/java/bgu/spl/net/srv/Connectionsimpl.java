package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connectionsimpl<T> implements Connections<T> {

    private HashMap<Integer, ConnectionHandler<T>> clients = new HashMap<>();
    private BookClubManager bookClubManager;
    private int connectionId=70;

    public Connectionsimpl(){
        bookClubManager=BookClubManager.getInstance();
    }

    @Override
    public boolean send(int connectionId, T msg) {
    //    try {
          //  lock.readLock().lock();
        boolean answer=false;
        if(this.getClients().containsKey(connectionId)) {
            ConnectionHandler<T> handler = clients.get(connectionId);
            handler.send((T) msg);
            answer = true;
        }
        return answer;
    }
      //  catch (Exception e) {
         //   return false;
     //   }

    @Override
    public void send(String genre, T msg) {
       //  lock.readLock().lock();
        if(bookClubManager.getgenres().containsKey(genre)){
            ConcurrentHashMap<String, LinkedList<Integer>> Genres=bookClubManager.getgenres();
            for(int i=0; i<Genres.get(genre).size(); i++){
                ConnectionHandler<T> handler = clients.get(i);
                handler.send((T)msg);
            }
        }
      //  lock.readLock().unlock();
    }

    @Override
    public void disconnect(int connectionId) {
        //try {
    //    lock.writeLock().lock();
        HashMap<String,String> genres = ((User)bookClubManager.getExistusers().get(connectionId)).getGenres();
        for(String s: genres.values()){
            //TODO
            ((LinkedList<Integer>)bookClubManager.getgenres().get(s)).remove(connectionId);
        }
        HashMap<String,String> newList = new HashMap<>();
        ((User) bookClubManager.getExistusers().get(connectionId)).setGenres(newList);
        ((User) bookClubManager.getExistusers().get(bookClubManager)).setConnect(false);
        if (clients.containsKey(connectionId))
        clients.remove(connectionId);
    //    lock.writeLock().unlock();
    }

    public int add(ConnectionHandler<T> connection) {
        int curID = this.connectionId++;
        clients.put(curID, connection);
        return curID;
    }

    public HashMap<Integer, ConnectionHandler<T>> getClients(){
        return clients;
    }
}
