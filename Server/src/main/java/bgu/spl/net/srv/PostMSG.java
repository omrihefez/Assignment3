package bgu.spl.net.srv;

public class PostMSG extends MSG{

    private String content;

    public PostMSG (String _content){
        super((short)5);
        content = _content;
    }

    public String getContent() {
        return content;
    }
}
