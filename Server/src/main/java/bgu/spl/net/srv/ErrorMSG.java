package bgu.spl.net.srv;

public class ErrorMSG extends MSG{

    private short messageOpcode;

    public ErrorMSG(short _opcode,short _messageOpcode){
        super(_opcode);
        messageOpcode = _messageOpcode;
    }

    public short getMessageOpcode() {
        return messageOpcode;
    }
}
