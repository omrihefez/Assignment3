package bgu.spl.net.srv;

public class AckMSG extends MSG {

    private short messageOpcode;

    public AckMSG(short _opcode,short _messageOpcode){
        super(_opcode);
        messageOpcode = _messageOpcode;
    }

    public short getMessageOpcode() {
        return messageOpcode;
    }
}
