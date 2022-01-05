package bgu.spl.net.srv;

public class PMMSG extends MSG {

    private String username;
    private String content;
    private String dateTime;

    public PMMSG (String _username, String _content, String _dateTime){
        super((short)6);
        username = _username;
        content = _content;
        dateTime = _dateTime;
    }

    public String getUsername(){
        return username;
    }
    public String getContent(){
        return content;
    }

    public String getDateTime() {
        return dateTime;
    }
}
