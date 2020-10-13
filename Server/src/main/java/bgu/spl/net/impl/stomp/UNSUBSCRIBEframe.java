package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.HashMap;

public class UNSUBSCRIBEframe extends Frame{

    //fields
    int topicId;
    int receiptId;
    //constructor
    public UNSUBSCRIBEframe(String command, HashMap<String , String> headers, String body, DataBase DB, int connectionId) {
        super(command,  headers, body, DB, connectionId);
    }

    public void process(){
        Connections connections = ConnectionsImpl.getInstance();
        topicId = Integer.parseInt(headers.get("id"));
        receiptId = Integer.parseInt(headers.get("receipt"));
        User user = DB.getUserByConnectionId(connectionId);
        String topicName = user.getSubscribedTo().get(topicId);
        //unsubscribe user to topic
        DB.unsubscribeUser(connectionId, topicName, topicId );
        //return to user RECEIPT for this frame
        connections.send(connectionId, buildRECEIPT(receiptId)); // sends RECEIPT to the user
    }
}
