package bgu.spl.net.srv;

public class NotificationMSG extends MSG {

    private int opcode;
    private int type;
    private String postingUser;
    private String content;

    public NotificationMSG(int _opcode, int _type, String _postingUser, String _content){
        opcode = _opcode;
        type = _type;
        postingUser = _postingUser;
        content = _content;
    }

}
