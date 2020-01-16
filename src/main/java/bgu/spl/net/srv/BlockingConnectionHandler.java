package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {


    private final StompMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private final Connectionsimpl<T> clients = new Connectionsimpl<>();

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, StompMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {
        System.out.println("conection handler is here");
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            System.out.println("trying to read a message");
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
           // System.out.println("almost here");
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                System.out.println("next message is "+nextMessage);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                    System.out.println("protocol is processing!!!");
                    if (protocol.shouldTerminate()) close();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) {
        try {
            System.out.println ("send: " + msg);
            out.write(encdec.encode(msg));
            out.flush();
            if (protocol.shouldTerminate()) close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StompMessagingProtocol<T> getProtocol() {
        return protocol;
    }

    public MessageEncoderDecoder<T> getEncdec() {
        return encdec;
    }
}
