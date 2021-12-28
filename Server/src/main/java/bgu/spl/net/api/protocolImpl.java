package bgu.spl.net.api;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

public class protocolImpl implements BidiMessagingProtocol {

    boolean shouldTerminate;
    ConnectionsImpl connections = ConnectionsImpl.getInstance();

    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Object message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
