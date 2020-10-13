package bgu.spl.net.srv;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;

public class ConnectionsImpl<T> implements Connections<T> {

    //fields
    private DataBase DB;
    private final Object lock;

    private static class SingletonHolder{
        private static ConnectionsImpl connections = new ConnectionsImpl<>();
    }

    private ConnectionsImpl() {
        DB = DataBase.getInstance();
        lock = new Object();
    }

    public static ConnectionsImpl getInstance(){return SingletonHolder.connections;}

    @Override
    public boolean send(int connectionId, T msg) {
        ConcurrentHashMap<Integer, ConnectionHandler<T>> clientsMap = DB.getClientsMap();
        if (!clientsMap.containsKey(connectionId))
            return false;
        ConnectionHandler ch = clientsMap.get(connectionId);
        //sync to prevent unregistering user while trying sending him a message
        synchronized (ch){
            if (ch != null){
                ch.send(msg);
                return true;
            }
        }
        return false;
    }

    @Override
    public void send(String channel, T msg) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topicsMap = DB.getTopicsMap();
        ConcurrentLinkedQueue<User> topicSubsQueue = topicsMap.get(channel);
        //sync to prevent unregistering user from a topic's map while trying to send him a message
        synchronized (topicSubsQueue){
            if (topicSubsQueue != null) {
                int size = topicSubsQueue.size();
                int index = 0;
                while (index < size) {
                    User user = topicSubsQueue.poll();
                    send(user.getId(), msg);
                    System.out.println(msg);
                    index++;
                    topicSubsQueue.add(user);
                }
            }
        }
    }

    @Override
    public void disconnect(int connectionId) {
        User userToDisconnect = DB.getUserByConnectionId(connectionId);
        ConcurrentHashMap<Integer, ConnectionHandler<T>> clientsMap = DB.getClientsMap();
        if (clientsMap.containsKey(connectionId)){
            ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topicsMap = DB.getTopicsMap();
            //remove User from all topics
           for (ConcurrentLinkedQueue<User> topicSubsMap : topicsMap.values()){
               //sync to prevent unregistering user from a topic's map while trying to send him a message
               synchronized (topicSubsMap){
                   if (topicSubsMap != null){
                       //remove User from this topic
                        topicSubsMap.remove(userToDisconnect);
                   }
               }
           }
            //sync to prevent unregistering user while trying to send him a message
           synchronized (clientsMap){
               clientsMap.remove(connectionId);
           }
           ConcurrentHashMap<Integer, User> UsersIntegerMap = DB.getUserIntegerMap();
           synchronized (UsersIntegerMap){
               synchronized (userToDisconnect){
                   userToDisconnect.setId(-1);
                   userToDisconnect.removeAllTopics();
               }
               UsersIntegerMap.remove(connectionId);
           }
        }
    }
}
