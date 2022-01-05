package bgu.spl.net.srv;

public class NotificationMSG extends MSG {

    private int type;
    private String postingUser;
    private String content;

    public NotificationMSG(int _type, String _postingUser, String _content){
        super((short)9);
        type = _type;
        postingUser = _postingUser;
        content = _content;
    }

    public int getType() {
        return type;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }
}
