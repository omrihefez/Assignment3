package bgu.spl.net.srv;

public class BlockMSG extends MSG{

    private int opcode;
    private String username;

    public BlockMSG(int _opcode, String _username){
        opcode = _opcode;
        username = _username;
    }

    public String getUsername() {
        return username;
    }
}
