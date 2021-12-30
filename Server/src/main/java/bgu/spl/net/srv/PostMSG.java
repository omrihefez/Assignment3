package bgu.spl.net.srv;

public class PostMSG extends MSG{

    private short opcode;
    private String content;

    public PostMSG (String _content){
        opcode = 5;
        content = _content;
    }
}
