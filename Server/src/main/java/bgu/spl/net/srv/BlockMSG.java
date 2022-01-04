package bgu.spl.net.srv;

public class BlockMSG extends MSG{

    private String username;

    public BlockMSG(short _opcode, String _username){
        super(_opcode);
        username = _username;
    }

    public String getUsername() {
        return username;
    }
}
