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

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public MSG decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            byte[] opCodeBytes = {bytes[0], bytes[1]};
            short opCode = bytesToShort(opCodeBytes);
            switch (opCode) {
                case 1: {
                    int i = 2;
                    while (bytes[i] != '\0')
                        i++;
                    String username = new String(bytes, 2, i, StandardCharsets.UTF_8);
                    int j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String password = new String(bytes, j, i, StandardCharsets.UTF_8);
                    j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String birthday = new String(bytes, j, i, StandardCharsets.UTF_8);
                    RegisterMSG registerMSG = new RegisterMSG(username, password, birthday);
                    return registerMSG;
                }
                case 2: {
                    int i = 2;
                    while (bytes[i] != '\0')
                        i++;
                    String username = new String(bytes, 2, i, StandardCharsets.UTF_8);
                    int j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String password = new String(bytes, j, i, StandardCharsets.UTF_8);
                    short captcha = bytesToShort(new byte[]{bytes[bytes.length - 1]});
                    LoginMSG loginMSG = new LoginMSG(username, password, captcha);
                    return loginMSG;

                }
                case 3: {
                    LogoutMSG logoutMSG = new LogoutMSG();
                    return logoutMSG;
                }
                case 4: {
                    short follow = bytes[2];
                    int i = 3;
                    while (bytes[i] != '\0')
                        i++;
                    String username = new String(bytes, 3, i, StandardCharsets.UTF_8);
                    FollowMSG followMSG = new FollowMSG(follow, username);
                    return followMSG;
                }
                case 5: {
                    int i = 2;
                    while (bytes[i] != '\0')
                        i++;
                    String content = new String(bytes, 2, i, StandardCharsets.UTF_8);
                    PostMSG postMSG = new PostMSG(content);
                    return postMSG;
                }
                case 6: {
                    int i = 2;
                    while (bytes[i] != '\0')
                        i++;
                    String username = new String(bytes, 2, i, StandardCharsets.UTF_8);
                    int j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String content = new String(bytes, j, i, StandardCharsets.UTF_8);
                    j = i++;
                    while (bytes[i] != '\0')
                        i++;
                    String dateTime = new String(bytes, j, i, StandardCharsets.UTF_8);
                    PMMSG pmMSG = new PMMSG(username, content, dateTime);
                    return pmMSG;
                }
                case 7: {
                    LogstatMSG logstatMSG = new LogstatMSG();
                    return logstatMSG;
                }
                case 8: {
                    int i = 2;
                    while (bytes[i] != '\0')
                        i++;
                    String usernames = new String(bytes, 2, i, StandardCharsets.UTF_8);
                    StatMSG statMSG = new StatMSG(usernames);
                    return statMSG;
                }
            }
        }
        else {
            if (len >= bytes.length)
                bytes = Arrays.copyOf(bytes, len * 2);
            bytes[len] = nextByte;
            len++;
        }
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        if (!(message instanceof MSG))
            return null;
        MSG msg = (MSG) message;
        byte[] output = null;
        if (msg instanceof NotificationMSG){
            NotificationMSG notificationMSG = (NotificationMSG) msg;
            String s = notificationMSG.getOpcode() + notificationMSG.getType() + notificationMSG.getPostingUser() + "\0" +
                    notificationMSG.getContent() + '\0' + ";";
            output = s.getBytes(StandardCharsets.UTF_8);
        }
        if (msg instanceof AckMSG){
            AckMSG ackMSG = (AckMSG) message;
            if (msg instanceof FollowAckMSG){
                FollowAckMSG followAckMSG = (FollowAckMSG) ackMSG;
                String s = followAckMSG.getOpcode() + followAckMSG.getMessageOpcode() + followAckMSG.getUsername() + '\0' + ";";
                output = s.getBytes(StandardCharsets.UTF_8);
            }
            else if (msg instanceof StatAckMSG){
                StatAckMSG statAckMSG = (StatAckMSG) ackMSG;
                String s = statAckMSG.getOpcode() + statAckMSG.getMessageOpcode() + statAckMSG.getAge() +
                        statAckMSG.getNumOfPosts() + statAckMSG.getNumOfFollowers() + statAckMSG.getNumOfFollowing() + ";";
                output = s.getBytes(StandardCharsets.UTF_8);
            }
            else{ //regular Ack message
                String s = ackMSG.getOpcode() + ackMSG.getMessageOpcode() + ";";
                output = s.getBytes(StandardCharsets.UTF_8);
            }
        }
        else if (msg instanceof ErrorMSG){
            ErrorMSG errorMSG = (ErrorMSG) msg;
            String s = errorMSG.getOpcode() + errorMSG.getMessageOpcode() + ";";
            output = s.getBytes(StandardCharsets.UTF_8);
        }
        else if (msg instanceof BlockMSG){
            BlockMSG blockMSG = (BlockMSG) msg;
            String s = blockMSG.getOpcode() + blockMSG.getUsername() + '\0' + ";";
            output = s.getBytes(StandardCharsets.UTF_8);
        }
        return output;
    }
}
