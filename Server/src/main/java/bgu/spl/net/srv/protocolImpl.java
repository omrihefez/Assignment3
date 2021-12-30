package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

public class protocolImpl implements BidiMessagingProtocol {

    public int clientID = 0;
    ConnectionsImpl connections = ConnectionsImpl.getInstance();

    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Object message) {
        MSG x = (MSG)message;
        switch (x.getOpcode()){
            case 1 : {
                RegisterMSG msg = (RegisterMSG)message;
                String username = msg.getUsername();
                String password = msg.getPassword();
                String birthday = msg.getBirthday();
                ClientInfo client = new ClientInfo(username,password,birthday);
                connections.addClient(client);
                out.write(encdec.encode(response));
                out.flush();
            }
        }

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
