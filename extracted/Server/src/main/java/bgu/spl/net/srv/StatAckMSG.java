package bgu.spl.net.srv;

public class StatAckMSG extends AckMSG{

    private short age;
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;


    public StatAckMSG(short _opcode, short _messageOpcode, short _age, short _numOfPosts, short _numOfFollowers, short _numOfFollowing) {
        super(_opcode, _messageOpcode);
        age = _age;
        numOfPosts = _numOfPosts;
        numOfFollowers = _numOfFollowers;
        numOfFollowing = _numOfFollowing;
    }

    public short getAge() {
        return age;
    }

    public short getNumOfPosts() {
        return numOfPosts;
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }

    public short getNumOfFollowing() {
        return numOfFollowing;
    }
}
