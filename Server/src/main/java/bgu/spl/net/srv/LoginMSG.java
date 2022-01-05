package bgu.spl.net.srv;

public class LoginMSG extends MSG{

    private String username;
    private String password;
    private short captcha;

    public LoginMSG(String _username, String _password, short _captcha){
        super((short)2);
        username = _username;
        password = _password;
        captcha = _captcha;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public short getCaptcha() {
        return captcha;
    }
}
