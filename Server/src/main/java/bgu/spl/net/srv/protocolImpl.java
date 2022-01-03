package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import com.sun.security.ntlm.Client;

import java.io.BufferedOutputStream;

public class protocolImpl implements BidiMessagingProtocol {

    ConnectionsImpl connections = ConnectionsImpl.getInstance();
    ConnectionHandlerImpl myHandler;

    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Object message) {
        MSG x = (MSG)message;
        switch (x.getOpcode()){
            case 1 : {
                RegisterMSG msg = (RegisterMSG) message;
                String username = msg.getUsername();
                String response;
                if (!connections.getUsernames().containsKey(username)) {
                    String password = msg.getPassword();
                    String birthday = msg.getBirthday();
                    ClientInfo client = new ClientInfo(username, password, birthday);
                    connections.addClient(client);
                    int clientID = (int) connections.getIdClientMap().get(client);
                    ConnectionHandlerImpl handler = (ConnectionHandlerImpl) connections.getConnectionsMap().get(clientID);
                    response = "10" + "1";
                }
                else {
                    response = "11" + "1";
                }
                BufferedOutputStream out = new BufferedOutputStream(handler.getSock().getOutputStream());
                out.write(handler.getEncdec().encode(response));
                out.flush();
            }
            case 2 : {
                LoginMSG msg = (LoginMSG) message;
                String username = msg.getUsername();
                String response;
                if (!connections.getUsernames().containsKey(username)) { //check if the client is Registered
                    response = "11" + "2";
                }
                else {
                  ClientInfo client = ((ClientInfo) connections.getUsernames().get(username));
                  if (!msg.getPassword().equals(client.getPassword()) | client.getLoggedIn() == true | msg.getCaptcha() == 0) // verify password, isLoggedIn, captcha
                      response = "11" + "2";
                  else {
                      client.setLoggedIn(true);
                      response = "10" + "2";
                  }
                }
            }
            case 3 : {
                LogoutMSG msg = (LogoutMSG) message;

            }
        }

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
