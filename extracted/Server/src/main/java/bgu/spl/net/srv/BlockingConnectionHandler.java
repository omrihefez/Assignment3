package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, bgu.spl.net.srv.bidi.ConnectionHandler<T> {

    private final BidiMessagingProtocol protocol;
    private final MessageEncoderDecoder encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private ConnectionsImpl connections;
    private int clientId;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol,
                                     ConnectionsImpl _connections, int _clientID) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        connections = _connections;
        clientId = _clientID;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            protocol.start(clientId, connections);

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                MSG nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
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
    public void send(Object msg) {
        if (msg == null || !(msg instanceof MSG))
            return;
        try {
            out.write(encdec.encode(msg));
            out.flush();
        } catch (Exception e){}
    }
}
