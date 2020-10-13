package bgu.spl.net.srv;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    //fields
    String userName;
    String password;
    int id;
    ConnectionHandler CH;
    ConcurrentHashMap<Integer, String> subscribedTo;  //<topicId, topic name>


    //constructor
    public User(ConnectionHandler CH, int id){
        this.CH = CH;
        this.id = id;
        this.subscribedTo = new  ConcurrentHashMap<Integer, String>();
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public ConnectionHandler getCH() {
        return CH;
    }

    public ConcurrentHashMap<Integer, String> getSubscribedTo() {
        return subscribedTo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSubscribedToTopic(String topic) {return subscribedTo.containsValue(topic); }

    public boolean isSubscribedToId(int topicId) {return subscribedTo.contains(topicId); }

    public synchronized void setTopic(String topic, int topicID) {
        subscribedTo.put(topicID, topic);
    }
    public synchronized void removeTopic(int topicID) {
        subscribedTo.remove(topicID);
    }
    public synchronized void removeAllTopics(){
        subscribedTo.clear();
    }
}

