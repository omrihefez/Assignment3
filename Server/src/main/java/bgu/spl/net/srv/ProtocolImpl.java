package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import com.sun.security.ntlm.Client;

import java.io.BufferedOutputStream;
import java.util.LinkedList;

public class ProtocolImpl implements BidiMessagingProtocol {

    private ConnectionsImpl connections;
    private int clientID;

    @Override
    public void start(int connectionId, Connections connections) {
        connections = connections;
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
                if (connections.getUsername(clientID) == null) {
                    String password = msg.getPassword();
                    String birthday = msg.getBirthday();
                    connections.setClient(clientID, username, password, birthday);
                    response = new AckMSG((short) 10, (short) 1);
                } else {
                    response = new ErrorMSG((short) 11, (short) 1);
                }
                connections.getHandler(clientID).send(response);
            }
            case 2: {
                LoginMSG msg = (LoginMSG) message;
                MSG response;
                if (connections.getUsername(clientID) == null)  //check if the client is Registered
                    response = new ErrorMSG((short) 11, (short) 2);
                else { // the client is Registered
                    ClientInfo client = connections.getClientInfo(clientID);
                    if (!msg.getPassword().equals(client.getPassword()) |
                            client.getLoggedIn() == true | msg.getCaptcha() == 0) // verify password, isLoggedIn, captcha
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
            }
            case 3: {
                ClientInfo client = connections.getClientInfo(clientID);
                MSG response;
                if (client == null || client.getLoggedIn() == false) // if not Registered | not logged in
                    response = new ErrorMSG((short) 11, (short) 3);
                else {
                    connections.getClientInfo(clientID).setLoggedIn(false);
                    response = new AckMSG((short) 10, (short) 3);
                }
                connections.getHandler(clientID).send(response);
            }
            case 4: {
                FollowMSG msg = (FollowMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                int toFollowID = connections.getClientId(msg.getUsername());
                if (client == null || client.getLoggedIn() == false |
                        toFollowID == -1) //check if both users are registered and follower is logged in
                    response = new ErrorMSG((short) 11, (short) 4);
                else if (msg.getFollow() == 0) { //follow
                    if (client.isFollowing(toFollowID)) //already following
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
            }
            case 5: {
                PostMSG msg = (PostMSG) message;
                MSG response = null;
                boolean error = false;
                LinkedList<Integer> loggedViewers = new LinkedList<>();
                LinkedList<Integer> unLoggedviewers = new LinkedList<>();
                ClientInfo client = connections.getClientInfo(clientID);
                if (client == null || client.getLoggedIn() == false) { // if not registered / logged in
                    response = new ErrorMSG((short) 11, (short) 5);
                    error = true;
                }
                String content = msg.getContent();
                for (int i = 0; i < content.length() & error == false; i++) {
                    if (content.charAt(i) == '@') {
                        String viewer = "";
                        for (int j = i++; j < content.length() && content.charAt(j) != ' '; j++) {
                            viewer += content.charAt(j);
                        }
                        int viewerID = connections.getClientId(viewer);
                        if (viewerID == -1) { //viewer is not registered
                            response = new ErrorMSG((short) 11, (short) 5);
                            error = true;
                        } else if (connections.getClientInfo(viewerID).getLoggedIn() == true)
                            loggedViewers.add(viewerID);
                        else
                            unLoggedviewers.add(viewerID);
                    }
                }
                if (!error) {
                    for (int ID : client.getFollowers()) {
                        if (connections.getClientInfo(ID).getLoggedIn() == true)
                            loggedViewers.add(ID);
                        else
                            unLoggedviewers.add(ID);
                    }
                    for (int ID : loggedViewers)
                        connections.getHandler(ID).send(new NotificationMSG(1, client.getUsername(), content));
                    for (int ID : unLoggedviewers)
                        connections.getClientInfo(ID).addPost(new NotificationMSG(1, client.getUsername(), content));
                    response = new AckMSG((short) 10, (short) 5);
                    client.increaseNumOfPosts();
                }
                connections.getHandler(clientID).send(response);
            }
            case 6: {
                PMMSG msg = (PMMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                int recipientID = connections.getClientId(msg.getUsername());
                if (client == null || client.getLoggedIn() == false | recipientID == -1) // if not registered / not logged in
                    response = new ErrorMSG((short) 11, (short) 6);
                else if (!client.isFollowing(recipientID))
                    response = new ErrorMSG((short) 11, (short) 6);
                else {
                    if (connections.getClientInfo(recipientID).getLoggedIn() == true)
                        connections.getHandler(recipientID).send(new NotificationMSG(0, client.getUsername(), msg.getContent()));
                    else
                        connections.getClientInfo(recipientID).addPost(new NotificationMSG(0, client.getUsername(), msg.getContent()));
                    response = new AckMSG((short)10, (short)6);
                }
                connections.getHandler(clientID).send(response);
            }
            case 7 : {
                LogstatMSG msg = (LogstatMSG) message;
                ClientInfo client = connections.getClientInfo(clientID);
                if (client == null || client.getLoggedIn() == false ) // if not registered / not logged in
                    connections.getHandler(clientID).send(new ErrorMSG((short) 11, (short) 7));
                else {
                    for (Object ID : connections.getLoggedUsers()) {
                        connections.getHandler(clientID).send(new StatAckMSG((short)10,(short)7,
                                client.getAge(),client.getNumOfPosts(),client.getNumOfFollowers(),client.getNumOfFollowing()));
                    }
                }
            }
            case 8 : {
                StatMSG msg = (StatMSG) message;
                MSG response;
                ClientInfo client = connections.getClientInfo(clientID);
                if (client == null || client.getLoggedIn() == false ) // if not registered / not logged in
                    response = new ErrorMSG((short) 11, (short) 8);
                else {
                    String usernames = msg.getUsernames();
                    for (int i = 0 ; i < usernames.length() ; i++){
                        String username = "";
                        while (usernames.charAt(i) != '|')
                            usernames +=
                    }
                }
            }


        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
