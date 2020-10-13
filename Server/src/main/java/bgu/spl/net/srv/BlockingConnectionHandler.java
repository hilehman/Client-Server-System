package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected;
    private final ConnectionsImpl<String> connections;
    private final int connectionId;
    private boolean isFirst;
    DataBase DB = DataBase.getInstance();



    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, StompMessagingProtocol<T> protocol, ConnectionsImpl<String> connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        connected = true;
        isFirst = true;
        this.connectionId = DB.addUser(this);

    }

    public int getConnectionID(){
        return  connectionId;
    }

    @Override
    public void run() {
        protocol.start(connectionId, connections);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process((String) nextMessage);
                    if(protocol.shouldTerminate()){
                        close();
                    }
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }

                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        protocol.setShouldTerminate(true);
        sock.close();
    }

    @Override
    public void send(T msg) {

        if (msg != null) {
            try {
                out.write(encdec.encode(msg));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
