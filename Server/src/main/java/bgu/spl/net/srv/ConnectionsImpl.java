package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class ConnectionsImpl <T> implements Connections<T> {

    private int clientID = 0;
    private HashMap<Integer, ConnectionHandlerImpl<T>> connectionsMap = new HashMap<>();
    private HashMap<ClientInfo, Integer> idClientMap = new HashMap<>();
    private HashMap <String, ClientInfo> usernames = new HashMap<>();
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

    public void addClient(ClientInfo client) {
        idClientMap.put(client, clientID);
        connectionsMap.put(clientID, new ConnectionHandlerImpl());//need to add parameters to constructor
        usernames.put(client.getUsername(),client);
        clientID++;
    }

    public HashMap<Integer, ConnectionHandlerImpl<T>> getConnectionsMap() {
        return connectionsMap;
    }

    public HashMap<ClientInfo, Integer> getIdClientMap() {
        return idClientMap;
    }

    public HashMap<String, ClientInfo> getUsernames() {
        return usernames;
    }
}
