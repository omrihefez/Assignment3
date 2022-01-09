package bgu.spl.net.srv;

public class RegisterMSG extends MSG {

    private String username;
    private String password;
    private String birthday;

    public RegisterMSG(String _username, String _password, String _birthday){
        super((short)1);
        username = _username;
        password = _password;
        birthday = _birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }
}

