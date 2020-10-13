package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.HashMap;

public class CONNECTframe extends Frame{

    public CONNECTframe(String command, HashMap<String , String> headers, String body, DataBase DB, int connectionId) {

        super(command,  headers, body, DB, connectionId);

    }

    public void process(){
        ConnectionsImpl connections = ConnectionsImpl.getInstance();
        String userName = getHeaders().get("login");
        String password = getHeaders().get("passcode");
        String version = getHeaders().get("accept-version");
        int exist = DB.isUserExist(userName);
        //check conditions for action
        if (exist == -2){//new user //TODO: done!
            User newUser = new User((ConnectionHandler) DB.getClientsMap().get(connectionId), connectionId);
            newUser.setPassword(password);
            newUser.setUserName(userName);
            System.out.println("created new user with name: " + userName +"   password: "+password +"     id: "+ connectionId);;
                       DB.getUserStringMap().put(userName, newUser);
            DB.getUserIntegerMap().put(connectionId,newUser); //
            // CONNECTED frame to the client and the client will print "Login successful”.
            connections.send(connectionId, buildCONNECTED(version)); // sends RECEIPT to the user
        }

        else if (exist == -1){//logged out //TODO: done!
            if (((User) DB.getUserStringMap().get(userName)).getPassword() == password){
                ((User) DB.getUserStringMap().get(userName)).setId(connectionId);
                DB.getUserIntegerMap().put(connectionId, DB.getUserStringMap().get(userName));
                //CONNECTED frame and the client will print to the screen "Login successful.”
                connections.send(connectionId, buildCONNECTED(version)); // sends RECEIPT to the user

            }
            else {//incorrect password TODO: done!
                connections.send(connectionId, buildERROR(version, "Wrong password"));
            }
        }
        else {//user is logged in TODO: done!
                connections.send(connectionId, buildERROR(version, "User is already logged in"));
            }
    }
    private String buildCONNECTED(String version){
        // CONNECTED frame to the client and the client will print "Login successful”.
        String command = "CONNECTED";
        String header = "version:";
        char newLine = '\n';
        char close = '\u0000';
        System.out.println("sending CONNECTED");
        return command+newLine+header+version+newLine+newLine+newLine+close;
    }
    private String buildERROR(String version, String message){
        char newLine = '\n';
        char close = '\u0000';
        return "ERROR" + newLine + "receipt-id:message" + version + newLine + "message:" + message + newLine+newLine+newLine+close;
    }
}
