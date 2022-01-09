package bgu.spl.net.srv;

public class MSG {

    private short opcode;

    public MSG(short _opcode){
        opcode = _opcode;
    }

    public short getOpcode(){
        return opcode;
    }
}
