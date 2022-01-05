package bgu.spl.net.srv;

import java.util.function.Supplier;

public class TcpServer extends BaseServer {

    public TcpServer(int port, Supplier protocolFactory, Supplier encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(ConnectionHandlerImpl handler) {
        new Thread(handler).start();
    }

    public static void main (String[] args){
        new TcpServer(Integer.valueOf(args[0]), () -> new ProtocolImpl(), () -> new EncoderDecoder()).serve();
    }

}
