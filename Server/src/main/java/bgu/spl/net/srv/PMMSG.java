package bgu.spl.net.srv;

public class PMMSG extends MSG {

    private short opcode;
    private String username;
    private String content;
    private String dateTime;

    public PMMSG (String _username, String _content, String _dateTime){
        opcode = 6;
        username = _username;
        content = _content;
        dateTime = _dateTime;
    }
}
