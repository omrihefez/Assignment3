package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandlerImpl<T> implements ConnectionHandler {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private Socket sock;

    public ConnectionHandlerImpl (Socket _sock, MessageEncoderDecoder<T> _reader,
                                  BidiMessagingProtocol<T> _protocol) {
        sock = _sock;
        encdec = _reader;
        protocol = _protocol;
    }

    public void run(){
        try (Socket sock = sock;
             BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream())) {
            int read;
            if (!protocol.shouldTerminate() && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                switch ((short) nextMessage){
                    case 0 :
                }
//                if (nextMessage != null) {
//                    T response = protocol.process(nextMessage);
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
//                }
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    @Override
    public void send(Object msg) {
        if (msg instanceof AckMSG){

        }
        else if (msg instanceof ErrorMSG){

        }
        else if (msg instanceof NotificationMSG){

        }
        else{

        }
    }

    @Override
    public void close() throws IOException {

    }

}
