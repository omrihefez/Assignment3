package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionHandlerImpl<T> implements Runnable,ConnectionHandler {

    private final ProtocolImpl protocol;
    private final EncoderDecoder encdec;
    private Socket sock = null;
    private ConnectionsImpl connections;
    private int clientID;
    private LinkedBlockingQueue<byte[]> outputQueue = new LinkedBlockingQueue<>();

    public ConnectionHandlerImpl(Socket _sock, EncoderDecoder _reader,
                                  ProtocolImpl _protocol, ConnectionsImpl _connections, int _clientID){
        sock = _sock;
        encdec = _reader;
        protocol = _protocol;
        connections = _connections;
        clientID = _clientID;
    }

    public Socket getSock() {
        return sock;
    }

    public EncoderDecoder getEncdec() {
        return encdec;
    }

    public void run() {
        protocol.start(clientID, connections);
        try (Socket sock = this.sock;
             BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream())) {
            int read;
            while (!protocol.shouldTerminate() && ((read = in.read()) >= 0) | !outputQueue.isEmpty()) {
                MSG nextMessage = null;
                if (nextMessage == null) {
                    nextMessage = encdec.decodeNextByte((byte) read);
                }
                if (nextMessage != null)
                    protocol.process(nextMessage);
                while (!outputQueue.isEmpty()) {
                    try {
                        out.write(outputQueue.poll());
                        out.flush();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

//                if (nextMessage != null) {
//                    T response = protocol.process(nextMessage);
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
//                }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public T getMessage(){
//
//    }

    @Override
    public void send(Object msg) {
        if (msg == null || !(msg instanceof MSG))
            return;
        try {
            byte[] encoded = encdec.encode(msg);
            if (encoded != null)
                outputQueue.put(encoded);
        } catch (Exception e) {}
    }

    public void sendCollection(Collection<MSG> messages){
        for (MSG message : messages)
            send(message);
    }

    @Override
    public void close() throws IOException {

    }

}
