package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class Connectionsimpl<T> implements Connections<T> {

    private final HashMap<Integer, ConnectionHandler<T>> clients = new HashMap<>();
    private HashMap<String, BlockingQueue<Integer>> genres = new HashMap<>();
    //  private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean send(int connectionId, T msg) {
    //    try {
          //  lock.readLock().lock();
        boolean answer=false;
            if(clients.containsKey(connectionId)) {
                ConnectionHandler<T> handler = clients.get(connectionId);
                // lock.readLock().unlock();
                handler.send((T) msg);
                answer = true;
            }
            return false;
        }
      //  catch (Exception e) {
         //   return false;
     //   }

    @Override
    public void send(String genre, T msg) {
       //  lock.readLock().lock();
        if(genres.containsKey(genre)){
            for(int i=0; i<genres.get(genre).size(); i++){
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
}
