package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SENDframe extends Frame{


    public SENDframe(String command, HashMap<String , String> headers, String body, DataBase DB, int connectionId) {
        super(command,  headers, body, DB, connectionId);

    }

    public void process(){
        int subid = 0;
        ConnectionsImpl connections = ConnectionsImpl.getInstance();
        String destination = getHeaders().get("destination");
        User user = DB.getUserByConnectionId(connectionId);
        ConcurrentHashMap<Integer, String> userTopics = user.getSubscribedTo();
        for (int topicID : userTopics.keySet()){
            if (userTopics.get(topicID).equals(destination)) {
                subid = topicID;
                break;
            }
        }
        int messageID = DB.getMessageid();
        connections.send(destination, buildMESSAGE(destination, subid, messageID, body)); // sends RECEIPT to the user
    }

    private String buildMESSAGE(String destination, int subID, int messageid, String body){
        // CONNECTED frame to the client and the client will print "Login successful”.
        String command = "MESSAGE";
        char newLine = '\n';
        char close = '\u0000';
        return command + newLine + "subscription:" + subID + newLine + "Message-id:" + messageid + newLine + "destination:" + destination + newLine + newLine+ body +newLine+newLine+newLine+close;
    }
}
