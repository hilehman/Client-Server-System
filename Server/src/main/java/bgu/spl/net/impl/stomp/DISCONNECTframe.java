package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;

import java.util.HashMap;

public class DISCONNECTframe extends Frame{

    public DISCONNECTframe(String command, HashMap<String , String> headers, String body, DataBase DB, int connectionId) {
        super(command,  headers, body, DB, connectionId);
    }

    public void process(){
        Connections connections = ConnectionsImpl.getInstance();
	int receiptId = Integer.parseInt(headers.get("receipt"));
        connections.send(connectionId, buildRECEIPT(receiptId)); // sends RECEIPT to the user
        connections.disconnect(connectionId);
    }
}
