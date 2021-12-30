package bgu.spl.net.srv;

public class RegisterMSG extends MSG {

    private short opcode;
    private String username;
    private String password;
    private String birthday;

    public RegisterMSG(String _username, String _password, String _birthday){
        opcode = 1;
        username = _username;
        password = _password;
        birthday = _birthday;
    }

}

