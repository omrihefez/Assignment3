package bgu.spl.net.srv;

public class StatMSG extends MSG{

    private String usernames;

    public StatMSG(String _usernames){
        super((short)8);
        usernames = _usernames;
    }
    public String getUsernames(){
        return usernames;
    }
}
