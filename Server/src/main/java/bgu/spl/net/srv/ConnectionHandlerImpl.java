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
    private final EncoderDecoder encdec;
    private Socket sock;

    public ConnectionHandlerImpl (Socket _sock, EncoderDecoder _reader,
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
            while (!protocol.shouldTerminate() && (read = in.read()) >= 0) {
                String nextMessage = null;
                while (nextMessage == null) {
                    nextMessage = (String)encdec.decodeNextByte((byte) read));
                }
            }

            while (!protocol.shouldTerminate() && (read = in.read()) >= 0) {
                T opCode0 = encdec.decodeNextByte((byte) read);
                T opCode1 = encdec.decodeNextByte((byte) read);
                short nextMessage = 0;
                if (opCode0 != null & opCode1 != null){
                    byte[] opCode = new byte[2];
                    opCode[0] = (byte)opCode0 ;
                    opCode[1] = (byte)opCode1;
                    nextMessage = bytesToShort(opCode);
                }
                switch (nextMessage){
                    case 1 : {
                        String username = "";
                        char nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            username += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        String password = "";
                        nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            password += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        String birthday = "";
                        nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            birthday += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        RegisterMSG registerMSG = new RegisterMSG(username, password, birthday);
                        protocol.process(registerMSG); //response will be written and sent in protocol
                    }
                    case 2 : {
                        String username = "";
                        char nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            username += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        String password = "";
                        nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            password += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        byte[] arr = {(byte)encdec.decodeNextByte((byte) read)};
                        short captcha = bytesToShort(arr);
                        LoginMSG loginMSG = new LoginMSG(username, password, captcha);
                        protocol.process(loginMSG);
                    }
                    case 3 : {
                        LogoutMSG logoutMSG = new LogoutMSG();
                        protocol.process(logoutMSG);
                    }
                    case 4 : {
                        byte[] arr = {(byte)encdec.decodeNextByte((byte) read)};
                        short follow = bytesToShort(arr);
                        String username = "";
                        T nextByte =  encdec.decodeNextByte((byte) read);
                        while (nextByte != null) {
                            char nextByteChar = byteToChar(nextByte);
                            username += nextByte;
                            nextByte = (encdec.decodeNextByte((byte) read));
                        }
                        FollowMSG followMSG = new FollowMSG(follow, username);
                        protocol.process(followMSG);
                    }
                    case 5 : {
                        String content = "";
                        char nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            content += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        PostMSG postMSG = new PostMSG(content);
                        protocol.process(postMSG);
                    }
                    case 6 : {
                        String username = "";
                        char nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            username += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        String content = "";
                        nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            content += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        String dateTime = "";
                        nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        while (nextByte != '\0') {
                            dateTime += nextByte;
                            nextByte = byteToChar(encdec.decodeNextByte((byte) read));
                        }
                        PMMSG pmmsg = new PMMSG(username, content, dateTime);
                        protocol.process(pmmsg); //response will be written and sent in protocol
                    }
                    case 7 : {
                        LogstatMSG logstatMSG = new LogstatMSG();
                        protocol.process(logstatMSG);
                    }
                    case 8 : {
                        StatMSG statmsg = new StatMSG();
                        protocol.process(statmsg);
                    }


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

    public T getMessage()

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
