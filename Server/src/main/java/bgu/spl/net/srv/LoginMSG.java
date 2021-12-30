package bgu.spl.net.srv;

public class LoginMSG extends MSG{

    private short opcode;
    private String username;
    private String password;
    private short captcha;

    public LoginMSG(String _username, String _password, short _captcha){
        opcode = 2;
        username = _username;
        password = _password;
        captcha = _captcha;
    }
}
