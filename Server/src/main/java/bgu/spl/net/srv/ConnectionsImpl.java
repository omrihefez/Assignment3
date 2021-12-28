package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.util.HashMap;


public class ConnectionsImpl <T> implements Connections<T> {

    private HashMap<Integer, ConnectionHandler<T>> connectionsMap = new HashMap<>();
    private static ConnectionsImpl instance = null;
    private static boolean isDone = false;

    private ConnectionsImpl (){
    }

    public static ConnectionsImpl getInstance() {
        if(isDone == false) {
            synchronized(ConnectionsImpl.class) {
                if(isDone == false) {
                    instance = new ConnectionsImpl();
                    isDone = true;
                }
            }
        }
        return instance;
    }

    public boolean send(int connectionId, T msg) {

        if (connectionsMap.containsKey(connectionId)) {
            connectionsMap.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    public void broadcast(T msg){
        for (int id : connectionsMap.keySet())
            connectionsMap.get(id).send(msg);
    }

    public void disconnect(int connectionId){
        connectionsMap.remove(connectionId);
    }
}
