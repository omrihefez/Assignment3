//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_STATACKMESSAGE_H
#define IDEAPROJECTS_STATACKMESSAGE_H


#include <AckMessage.h>

class StatAckMessage : public AckMessage {

private:
    short age;
    short numOfPosts;
    short numOfFollowers;
    short numOfFollowing;

public:
    StatAckMessage(short _messageOpcode, short _age, short _numOfPosts, short _numOfFollowers, short _numOfFollowing);
    short getAge();
    short getNumOfPosts();
    short getNumOfFollowers();
    short getNumOfFollowing();

};


#endif //IDEAPROJECTS_STATACKMESSAGE_H
