package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.StompFrame;
import bgu.spl.net.impl.stomp.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookClubManager<T> {

    private Connectionsimpl<StompFrame> connections = null;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> Genres;
    private HashMap<Integer,User> logedinusers;
    private HashMap<Integer,User> existusers;
    // private HashMap<Integer, ConnectionHandler<T>> clients;

    private BookClubManager(){
        Genres=new ConcurrentHashMap<>();
        logedinusers=new HashMap<Integer,User>();
        existusers=new HashMap<Integer,User>();
    }

    private static class Holder {
        private static BookClubManager dataBase = new BookClubManager();
    }
    public static BookClubManager getInstance() {
        return Holder.dataBase;
    }

    public HashMap<Integer,User> getLogedinusers(){
        return logedinusers;
    }

    public HashMap<Integer,User> getExistusers(){
        return existusers;
    }

    public HashMap<Integer, ConnectionHandler<StompFrame>> getClients(){
        return connections.getClients();
    }

    public ConcurrentHashMap<String,ConcurrentLinkedQueue<Integer>> getgenres(){
        return Genres;
    }

    public void initialConnections(Connectionsimpl<StompFrame> connections) {
        if (this.connections == null)
            this.connections = connections;
    }

   /* public boolean checklogin(User user){
        return logedinusers.contains(user.getConnectionId());
    }

    public boolean checkexist(Integer connectionId){
        return existusers.contains(connections.getConnectionId());
    }
*/
    public void joinReadingClub(User user,String genres){
        if(Genres.containsKey(genres)){
            ConcurrentLinkedQueue<Integer> users=Genres.get(genres);
            users.add(user.getConnectionId());
        }
        else {
            ConcurrentLinkedQueue<Integer> users=new ConcurrentLinkedQueue<>();
            users.add(user.getConnectionId());
            Genres.put(genres,users);
        }
    }

    public void ExitReadingClub(User user,String genre){
        if(Genres.containsKey(genre)){
            ConcurrentLinkedQueue<Integer> users=Genres.get(genre);
            if(users.contains(user.getConnectionId())){
                users.remove(user.getConnectionId());
            }
        }
    }

}