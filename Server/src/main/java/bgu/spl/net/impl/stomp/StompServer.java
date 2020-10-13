package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.STOMPMessageEncoderDecoder;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.StompMessagingProtocolImpl;

public class StompServer {

    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);

        if(args[1].equals("tpc")){
            Server.threadPerClient(
                    Integer.parseInt(args[0]),  //port
                    ()-> new StompMessagingProtocolImpl(),
                    STOMPMessageEncoderDecoder::new).serve();
        }
        if(args[1].equals("reactor")){
            Server.reactor(
                   Runtime.getRuntime().availableProcessors(),
                    Integer.parseInt(args[0]), //port
                    () ->  new StompMessagingProtocolImpl(),
                    STOMPMessageEncoderDecoder::new).serve();

       }

    }


}