package bgu.spl.net.srv;

public class FollowMSG extends MSG {

    private short follow;
    private String username;

    public FollowMSG (short _follow, String _username){
        super((short)4);
        follow = _follow;
        username = _username;
    }

    public String getUsername() {
        return username;
    }

    public short getFollow() {
        return follow;
    }
}
