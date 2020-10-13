package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBase<T> {
    //fields
  //  private ConcurrentHashMap<Integer, ConcurrentHashMap<String, Integer>> subscribersMap;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topicsMap;  // for each topic, holds inner map like this: <User>
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> clientsMap; //<connectionId, handler>
    private ConcurrentHashMap<String, User> userStringMap; // <userName, user>
    private ConcurrentHashMap<Integer, User> userIntegerMap;  //<connectionId, user>
    AtomicInteger nextid;
    AtomicInteger messageid;

    private static class SingletonHolder {
        private static DataBase dataBase = new DataBase();
    }

    private DataBase(){
        nextid = new AtomicInteger(0);
        messageid = new AtomicInteger(0);
        topicsMap = new ConcurrentHashMap<>();
        clientsMap = new ConcurrentHashMap<>();
        userStringMap = new ConcurrentHashMap<>();
        userIntegerMap = new ConcurrentHashMap<>();
    }

    public int getMessageid() {
        return messageid.incrementAndGet();
    }

    public static DataBase getInstance(){ return SingletonHolder.dataBase;}


    public ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> getTopicsMap() {
        return topicsMap;
    }

    public ConcurrentHashMap<Integer, ConnectionHandler<T>> getClientsMap() {
        return clientsMap;
    }

    public ConcurrentHashMap<String, User> getUserStringMap() {
        return userStringMap;
    }
    public ConcurrentHashMap<Integer, User> getUserIntegerMap() { return userIntegerMap; }


    public int addUser(ConnectionHandler CH){
        int id = nextid.get();
        nextid.getAndIncrement();
        synchronized (clientsMap){
            clientsMap.put(id,CH);
        }
        return id ;
    }

    public int isUserExist(String userName){
            if (userStringMap.containsKey(userName)){
                return  userStringMap.get(userName).getId();
             }
        return -2;
    }


    public User getUserByName(String name){
        return userStringMap.get(name);
    }

    public User getUserByConnectionId(int connectionId){
        return userIntegerMap.get(connectionId);
           }

    public void subscribeUser(int connectionId, String topic, int topicId){
        User user = getUserByConnectionId(connectionId);
        if(!user.isSubscribedToTopic(topic)) {// if user is not already subscribed to this topic
            user.setTopic(topic, topicId); //add the topic to the user
            synchronized (user) {
                if (topicsMap.get(topic) == null) {
                    topicsMap.put(topic, new ConcurrentLinkedQueue<User>());
                }
                topicsMap.get(topic).add(user); //add the user to the topic
            }
        }
    }

    public void unsubscribeUser(int connectionId, String topic, int topicId){
        User user = getUserByConnectionId(connectionId);
        synchronized (user){
            if(user.isSubscribedToTopic(topic)) {// if user is subscribed to this topic
                topicsMap.get(topic).remove(user); //removes the user from the topic
                user.removeTopic(topicId); //removes the topic from the user
            }
        }
    }
}

