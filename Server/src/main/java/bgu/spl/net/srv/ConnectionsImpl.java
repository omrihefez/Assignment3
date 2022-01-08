package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;


public class ConnectionsImpl <T> implements Connections<T> {

    private HashMap<Integer, ConnectionHandlerImpl<T>> connectionsMap = new HashMap<>();
    private HashMap<Integer, ClientInfo> idClientMap = new HashMap<>();
    private Vector<String> filter;

//    private static ConnectionsImpl instance = null;
//    private static boolean isDone = false;

    public ConnectionsImpl(Vector<String> _filter){
        filter = _filter;
    }

//    public static ConnectionsImpl getInstance() {
//        if(isDone == false) {
//            synchronized(ConnectionsImpl.class) {
//                if(isDone == false) {
//                    instance = new ConnectionsImpl();
//                    isDone = true;
//                }
//            }
//        }
//        return instance;
//    }

    public boolean send(int connectionId, T msg) {
        if (connectionsMap.containsKey(connectionId)) {
            connectionsMap.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    public void setClient(int _clientID, String username, String password, String birthday){
        ClientInfo client = new ClientInfo(username, password, birthday);
        addClientInfo(_clientID, client);
    }

    public void broadcast(T msg){
        for (int id : connectionsMap.keySet())
            connectionsMap.get(id).send(msg);
    }

    public void disconnect(int connectionId){
        connectionsMap.remove(connectionId);
        idClientMap.remove(connectionId);

    }

    public ConnectionHandlerImpl getHandler(int clientId){
        return connectionsMap.get(clientId);
    }

    public void addClient(int clientId, ConnectionHandlerImpl connectionHandler) {
        connectionsMap.put(clientId, connectionHandler);
    }

    public HashSet<Integer> getLoggedUsers(){
        HashSet<Integer> loggedUsers = new HashSet<>();
        for (int i : idClientMap.keySet())
            if (idClientMap.get(i).getLoggedIn() == true)
                loggedUsers.add(i);
        return loggedUsers;
    }

    public void addClientInfo(int clientId, ClientInfo clientInfo){
        idClientMap.put(clientId, clientInfo);
    }

    public ClientInfo getClientInfo(int clientId){
        return idClientMap.get(clientId);
    }

    public int getClientId(ClientInfo clientInfo){
        for (HashMap.Entry<Integer, ClientInfo> entry : idClientMap.entrySet())
            if (clientInfo == entry.getValue())
                return entry.getKey();
        return -1;
    }

    public HashMap<Integer, ConnectionHandlerImpl<T>> getConnectionsMap() {
        return connectionsMap;
    }

    public String getUsername(int clientId) {
        if (idClientMap.get(clientId) != null)
            return idClientMap.get(clientId).getUsername();
        return null;
    }

    public int getClientId(String username){
        for (HashMap.Entry<Integer, ClientInfo> entry : idClientMap.entrySet())
            if (username.equals(entry.getValue().getUsername()))
                return entry.getKey();
        return -1;
    }

    public Vector<String> getFilter(){
        return filter;
    }
}
