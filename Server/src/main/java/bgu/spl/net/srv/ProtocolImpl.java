package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
//import bgu.spl.net.srv.ConnectionsImpl;

//import java.io.BufferedOutputStream;
import java.util.LinkedList;
//import java.util.Vector;

public class ProtocolImpl implements BidiMessagingProtocol {

    private ConnectionsImpl connections;
    private int clientID;

    @Override
    public void start(int connectionId, Connections _connections) {
        connections = (ConnectionsImpl) _connections;
        clientID = connectionId;
    }

    @Override
    public void process(Object message) {
        MSG x = (MSG) message;
        switch (x.getOpcode()) {
            case 1: {
                RegisterMSG msg = (RegisterMSG) message;
                String username = msg.getUsername();
                MSG response;
                if (connections.getUsername(clientID) == null & connections.getClientId(username) == -1) {
                    String password = msg.getPassword();
                    String birthday = msg.getBirthday();
                    connections.setClient(clientID, username, password, birthday);
                    response = new AckMSG((short) 10, (short) 1);
                } else {
                    response = new ErrorMSG((short) 11, (short) 1);
                }
                connections.getHandler(clientID).send(response);
                break;
            }
            case 2: {
                LoginMSG msg = (LoginMSG) message;
                MSG response;
                if (connections.getClientId((msg).getUsername()) == -1)  //check if the client is Registered
                    response = new ErrorMSG((short) 11, (short) 2);
                else { // the client is Registered
                    ClientInfo client = connections.getClientInfo(clientID);
                    if (!msg.getPassword().equals(client.getPassword()) |
                            client.getLoggedIn() | msg.getCaptcha() == 0) // verify password, isLoggedIn, captcha
                        response = new ErrorMSG((short) 11, (short) 2);
                    else {
                        client.setLoggedIn(true);
                        response = new AckMSG((short) 10, (short) 2);
                    }
                }
                connections.getHandler(clientID).send(response);
                while (!connections.getClientInfo(clientID).getPostsToView().isEmpty()){
                    connections.getHandler(clientID).send(connections.getClientInfo(clientID).getPostsToView().poll());
                }
                break;
            }
            case 3: {
                ClientInfo client = connections.getClientInfo(clientID);
                MSG response;
                if (client == null || !client.getLoggedIn()) // if not Registered | not logged in
                    response = new ErrorMSG((short) 11, (short) 3);
                else {
                    connections.getClientInfo(clientID).setLoggedIn(false);
                    response = new AckMSG((short) 10, (short) 3);
                }
                connections.getHandler(clientID).send(response);
                break;
            }
            case 4: {
                    FollowMSG msg = (FollowMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                int toFollowID = connections.getClientId(msg.getUsername());
                if (client == null || !client.getLoggedIn() |
                        toFollowID == -1) //check if both users are registered and follower is logged in
                    response = new ErrorMSG((short) 11, (short) 4);
                else if (msg.getFollow() == 0) { //follow
                    if (client.isFollowing(toFollowID)) //already following
                        response = new ErrorMSG((short) 11, (short) 4);
                    else if (client.isBlocked(toFollowID)) //blocked
                        response = new ErrorMSG((short) 11, (short) 4);
                    else {
                        client.follow(toFollowID);
                        connections.getClientInfo(toFollowID).addFollower(clientID);
                        response = new FollowAckMSG((short) 10, (short) 4, connections.getUsername(toFollowID));
                    }
                } else { //unfollow
                    if (!client.isFollowing(toFollowID)) //isn't following
                        response = new ErrorMSG((short) 11, (short) 4);
                    else {
                        client.unFollow(toFollowID);
                        connections.getClientInfo(toFollowID).removeFollower(clientID);
                        response = new FollowAckMSG((short) 10, (short) 4, connections.getUsername(toFollowID));
                    }
                }
                connections.getHandler(clientID).send(response);
                break;
            }
            case 5: {
                PostMSG msg = (PostMSG) message;
                MSG response = null;
                LinkedList<Integer> loggedViewers = new LinkedList<>();
                LinkedList<Integer> unLoggedViewers = new LinkedList<>();
                ClientInfo client = connections.getClientInfo(clientID);
                if (client == null || !client.getLoggedIn()) // if not registered / logged in
                    response = new ErrorMSG((short) 11, (short) 5);
                else {
                    String content = msg.getContent();
                    for (int i = 0; i < content.length() ; i++) {
                        if (content.charAt(i) == '@') {
                            String viewer = "";
                            for (int j = i++; j < content.length() && content.charAt(j) != ' '; j++) {
                                viewer += content.charAt(j);
                            }
                            int viewerID = connections.getClientId(viewer);
                            if (viewerID != -1) { //viewer is registered
                                if (connections.getClientInfo(viewerID).getLoggedIn())
                                    loggedViewers.add(viewerID);
                                else
                                    unLoggedViewers.add(viewerID);
                            }
                        }
                    }
                    for (int ID : client.getFollowers()) {
                        if (connections.getClientInfo(ID).getLoggedIn())
                            loggedViewers.add(ID);
                        else
                            unLoggedViewers.add(ID);
                    }
                    for (int ID : loggedViewers) {
                        if (!client.isBlocked(ID))
                            connections.getHandler(ID).send(new NotificationMSG(1, client.getUsername(), content));
                    }
                    for (int ID : unLoggedViewers) {
                        if (!client.isBlocked(ID))
                            connections.getClientInfo(ID).addPost(new NotificationMSG(1, client.getUsername(), content));
                    }
                    response = new AckMSG((short) 10, (short) 5);
                    client.increaseNumOfPosts();
                }
                connections.getHandler(clientID).send(response);
                break;
            }
            case 6: {
                PMMSG msg = (PMMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                int recipientID = connections.getClientId(msg.getUsername());
                if (client == null || !client.getLoggedIn() | recipientID == -1) // if not registered / not logged in
                    response = new ErrorMSG((short) 11, (short) 6);
                else if (!client.isFollowing(recipientID))
                    response = new ErrorMSG((short) 11, (short) 6);
                else {
                    if (connections.getClientInfo(recipientID).getLoggedIn())
                        connections.getHandler(recipientID).send(new NotificationMSG(0, client.getUsername(), msg.getContent(connections.getFilter())+ msg.getDateTime()));
                    else
                        connections.getClientInfo(recipientID).addPost(new NotificationMSG(0, client.getUsername(), msg.getContent(connections.getFilter()) + msg.getDateTime()));
                    response = new AckMSG((short)10, (short)6);
                }
                connections.getHandler(clientID).send(response);
                break;
            }
            case 7 : {
                LogstatMSG msg = (LogstatMSG) message;
                ClientInfo client = connections.getClientInfo(clientID);
                if (client == null || !client.getLoggedIn()) // if not registered / not logged in
                    connections.getHandler(clientID).send(new ErrorMSG((short) 11, (short) 7));
                else {
                    for (Object ID : connections.getLoggedUsers()) {
                        if (!client.isBlocked((int)ID)) {
                            ClientInfo checkedClient = connections.getClientInfo((int) ID);
                            connections.getHandler(clientID).send(new StatAckMSG((short) 10, (short) 7, checkedClient.getAge(),
                                    checkedClient.getNumOfPosts(), checkedClient.getNumOfFollowers(), checkedClient.getNumOfFollowing()));
                        }
                    }
                }
                break;
            }
            case 8 : {
                StatMSG msg = (StatMSG) message;
                ClientInfo client = connections.getClientInfo(clientID);
                LinkedList<String> usernamesList = new LinkedList<>();
                if (client == null || !client.getLoggedIn()) // if not registered / not logged in
                    connections.getHandler(clientID).send(new ErrorMSG((short) 11, (short) 8));
                else {
                    String usernames = msg.getUsernames();
                    int i = 0;
                    while (i < usernames.length()){
                        String username = "";
                        for (int j = i ; j < usernames.length() && usernames.charAt(j) != '|' ; j++) {
                            username += usernames.charAt(j);
                            i++;
                        }
                        usernamesList.add(username);
                        i++;
                    }
                    for (String s : usernamesList){
                        if (connections.getClientId(s) != -1 && !connections.getClientInfo(connections.getClientId(s)).isBlocked(clientID)) {
                            int ID = connections.getClientId(s);
                            if (!client.isBlocked(ID)) {
                                ClientInfo checkedClient = connections.getClientInfo(ID);
                                connections.getHandler(clientID).send(new StatAckMSG((short) 10, (short) 8, checkedClient.getAge(),
                                        checkedClient.getNumOfPosts(), checkedClient.getNumOfFollowers(), checkedClient.getNumOfFollowing()));
                            }
                        }
                        else
                            connections.getHandler(clientID).send(new ErrorMSG((short) 11, (short) 8));
                    }
                }
                break;
            }
            case 12 : {
                BlockMSG msg = (BlockMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                int toBlockID = connections.getClientId(msg.getUsername());
                if (client == null || !client.getLoggedIn() | toBlockID == -1) // if not registered / not logged in
                    connections.getHandler(clientID).send(new ErrorMSG((short) 11, (short) 12));
                else {
                    ClientInfo toBlock = connections.getClientInfo(toBlockID);
                    if (client.isFollowing(toBlockID)) {
                        client.unFollow(toBlockID);
                        toBlock.removeFollower(clientID);
                    }
                    if (toBlock.isFollowing(clientID)) {
                        toBlock.unFollow(clientID);
                        client.removeFollower(toBlockID);
                    }
                    client.block(toBlockID);
                    toBlock.block(clientID);
                    connections.getHandler(clientID).send(new AckMSG((short)10,(short)12));
                }
                break;
            }


        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
