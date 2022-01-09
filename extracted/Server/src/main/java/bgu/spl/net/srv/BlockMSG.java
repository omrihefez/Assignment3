package bgu.spl.net.srv;

public class BlockMSG extends MSG{

    private String username;

    public BlockMSG(String _username){
        super((short)12);
        username = _username;
    }

    public String getUsername() {
        return username;
    }
}
