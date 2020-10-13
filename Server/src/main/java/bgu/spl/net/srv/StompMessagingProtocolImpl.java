package bgu.spl.net.srv;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<String> {
    //fields
    boolean shouldTerminate;
    int userCounter;
    Connections<String> connections;
    BufferedReader bufferedReader;
    DataBase DB;
    int connectionId;

    //constructor
    public StompMessagingProtocolImpl() {
        shouldTerminate = false;
        userCounter = 0;
        DB = DataBase.getInstance();
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this. connections = connections;
    }

    @Override
    public void process(String message)  {

        List<String> words = new ArrayList<>(Arrays.asList(message.split("\n")));


        //gets command
        String command;
        int i = 0;
        if (words.get(0).length()==0) i = 1;
        command = words.get(i);
        i++;
        // gets headers
        HashMap<String, String> headers = new HashMap();
        String header;
        System.out.println("got the command: " + command);

        while (i<words.size() && (words.get(i)).length()>0) {
            header = words.get(i);
            int index = header.indexOf(':');
            if(index != -1) {
                String key = header.substring(0, index);
                String value = header.substring(index + 1, header.length());
                headers.put(key.trim(), value.trim());
                ;
            }
            i++;
        }
        // gets rid of empty lines
        while (i<words.size() && ((header = words.get(i)).length()==0)) {
            String temp = words.get(i);
            i++;
        }

        // gets body
        String bodyAsString = "";
        while(i<words.size()){
            bodyAsString = bodyAsString + words.get(i) +" ";
            i++;
        }
        //     String bodyAsString = bufferedMessage.readLine();


        if (command.equals("CONNECT")) {
            System.out.println("got CONNECT");
            Frame newFrame = new CONNECTframe(command, headers, bodyAsString, DB, connectionId);
            newFrame.process();
            //     login 127.0.0.1:7777 hillel 1234
        }
        else if (command.equals("SEND")) {
            Frame newFrame = new SENDframe(command, headers, bodyAsString, DB, connectionId);
            newFrame.process();
        } else if (command.equals("SUBSCRIBE")) {
            Frame newFrame = new SUBSCRIBEframe(command, headers, bodyAsString, DB, connectionId);
            newFrame.process();
        } else if (command.equals("UNSUBSCRIBE")) {
            Frame newFrame = new UNSUBSCRIBEframe(command, headers, bodyAsString, DB, connectionId);
            newFrame.process();
        } else if (command.equals("DISCONNECT")) {
            Frame newFrame = new DISCONNECTframe(command, headers, bodyAsString, DB, connectionId);
            newFrame.process();
        }
    }
    @Override
    public boolean shouldTerminate() { return shouldTerminate; }


    public void setShouldTerminate(boolean b) {

    }
}
