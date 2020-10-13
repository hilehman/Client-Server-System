package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SUBSCRIBEframe extends Frame{

    String topicToSubscribe;
    int topicId;
    int receiptId;

    public SUBSCRIBEframe(String command, HashMap<String , String> headers, String body, DataBase DB, int connectionId) {
        super(command,  headers, body, DB, connectionId);
    }

    public void process(){
        Connections connections = ConnectionsImpl.getInstance();
        topicToSubscribe = headers.get("destination");
        topicId = Integer.parseInt(headers.get("id"));
        receiptId = Integer.parseInt(headers.get("receipt"));
        //subscribe user to topic
        DB.subscribeUser(connectionId, topicToSubscribe,topicId);
        System.out.println("subscribed "+((User)DB.getUserIntegerMap().get(connectionId)).getUserName() +" to " + topicToSubscribe);
        //return to user RECEIPT for this frame
        connections.send(connectionId, buildRECEIPT(receiptId)); // sends RECEIPT to the user
    }
}

