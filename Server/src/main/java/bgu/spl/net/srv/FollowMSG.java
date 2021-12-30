package bgu.spl.net.srv;

public class FollowMSG extends MSG {

    private short opcode;
    private short follow;
    private String username;

    public FollowMSG (short _follow, String _username){
        opcode = 4;
        follow = _follow;
        username = _username;
    }
}
