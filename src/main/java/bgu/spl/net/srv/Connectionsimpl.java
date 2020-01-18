package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.StompFrame;
import bgu.spl.net.impl.stomp.User;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connectionsimpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> clients = new ConcurrentHashMap<>();
    private BookClubManager bookClubManager;
    private int connectionId = 70;

    private static class SingletonConnectionImpl {
        private static Connectionsimpl instance = new Connectionsimpl();
    }

    public static Connectionsimpl getInstance() {
        return SingletonConnectionImpl.instance;
    }

    public Connectionsimpl() {
        bookClubManager = BookClubManager.getInstance();
    }


    @Override
    public boolean send(int connectionId, T msg) {
        //    try {
        //  lock.readLock().lock();
        //       System.out.println("sending the messageeee "+msg.toString());
        boolean answer = false;
        if (this.getClients().containsKey(connectionId)) {
            ConnectionHandler<T> handler = clients.get(connectionId);
            handler.send(msg);
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
        if (bookClubManager.getgenres().containsKey(genre)) {
            ConcurrentHashMap<String, LinkedList<Integer>> Genres = bookClubManager.getgenres();
            LinkedList<Integer> subscribers=Genres.get(genre);
            for (int i = 0; i < subscribers.size(); i++) {
                ConnectionHandler<T> handler = clients.get(subscribers.get(i));
                handler.send((T) msg);
            }
        }
        //  lock.readLock().unlock();
    }

    @Override
    public void disconnect(int connectionId) {
        //try {
        //    lock.writeLock().lock();
        ConcurrentHashMap<String, String> genres = ((User) bookClubManager.getExistusers().get(connectionId)).getGenres();
        for (String s : genres.values()) {
            //TODO
            ((LinkedList<Integer>) bookClubManager.getgenres().get(s)).remove(connectionId);
        }
        ConcurrentHashMap<String, String> newList = new ConcurrentHashMap<>();
        ((User) bookClubManager.getExistusers().get(connectionId)).setGenres(newList);
        ((User) bookClubManager.getExistusers().get(bookClubManager)).setConnect(false);
        if (clients.containsKey(connectionId))
            clients.remove(connectionId);
        //    lock.writeLock().unlock();
    }

    public int add(ConnectionHandler<T> connectionhadler) {
        int curID = this.connectionId++;
        clients.put(curID, connectionhadler);


        return curID;
    }

    public ConcurrentHashMap<Integer, ConnectionHandler<T>> getClients() {
        return clients;
    }


    @Override
    public String toString() {
        String output = "--CLIENT_HENDLER--: \n";
        for (Integer conID : this.getClients().keySet()) {
            output = output + "conID: " + conID + "\n";
        }
        output = output + "\n--GENRES--: \n";
        ConcurrentHashMap<String, List<Integer>> genres = bookClubManager.getgenres();
        for (String genre : genres.keySet()) {
            output = output + genre + ":\t";
            for (Integer id : genres.get(genre)) {
                output += id + ", ";
            }
            output = output + "\n";
        }
        output = output + "\n--CLIENT_INFO--: \n";
        ConcurrentHashMap<Integer, List<Integer>> clientsInfo = bookClubManager.getExistusers();
        for (Integer clientId : clientsInfo.keySet()) {
            output = output + clientId + ":\n" + clientsInfo.get(clientId) + "\n";
        }
        return output;

    }
}