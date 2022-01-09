package bgu.spl.net.srv;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientInfo {

    private String username;
    private String password;
    private String birthday;
    private short age;
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;
    private boolean isLoggedIn;
    private HashSet<Integer> followers;
    private HashSet<Integer> following;
    private HashSet<Integer> blocked;
    private LinkedBlockingQueue<NotificationMSG> postsToView;

    public ClientInfo(String _username, String _password, String _birthday){
        username = _username;
        password = _password;
        birthday = _birthday;
        age = (short) birthdayToAge(birthday);
        numOfPosts = 0;
        numOfFollowers = 0;
        numOfFollowing = 0;
        isLoggedIn = false;
        followers = new HashSet<>();
        following = new HashSet<>();
        blocked = new HashSet<>();
        postsToView = new LinkedBlockingQueue<>();
    }

    private int birthdayToAge(String birthday){
        int day = Integer.parseInt(birthday.substring(0,2));
        int month = Integer.parseInt(birthday.substring(3,5));
        int year = Integer.parseInt(birthday.substring(6,birthday.length()));
        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.of(year,month,day);
        return Period.between(birthDate, today).getYears();
    }

    public void addPost(NotificationMSG post){
        postsToView.add(post);
    }
    public void increaseNumOfPosts(){
        numOfPosts++;
    }

    public short getAge(){
        return age;
    }
    public short getNumOfPosts(){
        return numOfPosts;
    }
    public boolean isBlocked(int ID){
        return blocked.contains(ID);
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }
    public short getNumOfFollowing(){
        return numOfFollowing;
    }

    public boolean isFollowing(int ID){
        return following.contains(ID);
    }

    public void follow(int ID){
        following.add(ID);
        numOfFollowing++;
    }
    public void unFollow(int ID){
        following.remove(ID);
        numOfFollowing--;
    }
    public void addFollower(int ID){
        followers.add(ID);
        numOfFollowers++;
    }
    public void removeFollower(int ID){
        followers.remove(ID);
        numOfFollowers--;
    }
    public void block(int ID){
        blocked.add(ID);
    }
    public HashSet<Integer> getFollowers() {
        return followers;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getLoggedIn() {
        return isLoggedIn;
    }

    public LinkedBlockingQueue<NotificationMSG> getPostsToView() {
        return postsToView;
    }

    public void setUsername(String _username){
        username = _username;
    }
    public void setPassword(String _password){
        password = _password;
    }
    public void setBirthday(String _birthday){
        birthday = _birthday;
    }

    public void setLoggedIn(boolean state){
        isLoggedIn = state;
    }
}
