package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.DataBase;

import java.util.HashMap;

public class Frame {

        protected final String command;
        protected final HashMap<String , String> headers;
        protected final String body;
        protected DataBase DB;
        protected int connectionId;


        public Frame(String command, HashMap<String , String> header, String body, DataBase DB, int connectionId) {
            this.command = command;
            this.headers = header;
            this.body = body;
            this.DB = DB;
            this.connectionId = connectionId;
        }

    public HashMap<String, String> getHeaders() {
        return headers;
    }


    public void process(){}

    public String buildRECEIPT(int receiptId){
        String command = "RECEIPT";
        String header = "receipt-id:";
        char newLine = '\n';
        char close = '\u0000';
        return command+newLine+header+receiptId+newLine+newLine+newLine+close;
    }
}

