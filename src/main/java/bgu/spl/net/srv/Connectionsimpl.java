package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connectionsimpl<T> implements Connections<T> {

    private final HashMap<Integer, ConnectionHandler<T>> clients = new HashMap<>();
   // private HashMap<String, BlockingQueue<Integer>> genres = new HashMap<>();
    //  private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private BookClubManager bookClubManager;
    private int id=70;

    public Connectionsimpl(){
        bookClubManager=BookClubManager.getInstance();
    }


    public HashMap<Integer, ConnectionHandler<T>> getClients(){
        return clients;
    }

    @Override
    public boolean send(int connectionId, T msg) {
    //    try {
          //  lock.readLock().lock();
        boolean answer=false;

            if(bookClubManager.getgenres().containsKey(connectionId)) {
                ConnectionHandler<T> handler = clients.get(connectionId);
                // lock.readLock().unlock();
                handler.send((T) msg);
                answer = true;
            }
            return false;
        }

        //TODO always return false
      //  catch (Exception e) {
         //   return false;
     //   }

    @Override
    public void send(String genre, T msg) {
       //  lock.readLock().lock();
        if(bookClubManager.getgenres().containsKey(genre)){
            ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> Genres=bookClubManager.getgenres();
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
        if (clients.containsKey(connectionId))
        clients.remove(connectionId);
    //    lock.writeLock().unlock();
    }


    public int add(ConnectionHandler<T> connection) {
        int curID = this.id++;
        clients.put(curID, connection);
        return curID;
    }
}
