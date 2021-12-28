package bgu.spl.net.srv;

public class AckMSG extends MSG {

    private int opcode;
    private int messageOpcode;

    public AckMSG(int _opcode,int _messageOpcode){
        opcode = _opcode;
        messageOpcode = _messageOpcode;
    }

}
