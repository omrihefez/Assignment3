package bgu.spl.net.srv;

public class FollowAckMSG extends AckMSG{

    private String username;

    public FollowAckMSG(short _opcode, short _messageOpcode, String _username) {
        super(_opcode, _messageOpcode);
        username = _username;
    }

    public String getUsername() {
        return username;
    }
}
