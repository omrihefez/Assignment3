package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    @Override
    public MSG decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            byte[] opCodeBytes = {bytes[0], bytes[1]};
            short opCode = bytesToShort(opCodeBytes);
            switch (opCode){
                case 1 : {
                    int i =2;
                    while (bytes[i] != '\0') 
                        i++;
                    String username = new String(bytes,2,i,StandardCharsets.UTF_8);
                    int j = i++;
                    while (bytes[i] != '\0')
                        i++;      
                    String password = new String(bytes,j,i,StandardCharsets.UTF_8); 
                    j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String birthday = new String(bytes,j,i,StandardCharsets.UTF_8);
                    RegisterMSG registerMSG = new RegisterMSG(username, password, birthday);
                    return registerMSG;
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
//            return new String(bytes,0,len, StandardCharsets.UTF_8);
        }
        else {
            if (len >= bytes.length)
                bytes = Arrays.copyOf(bytes, len*2);
            bytes[len] = nextByte;
            len++;
        }
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
