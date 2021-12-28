//
// Created by Omri on 28/12/2021.
//

#include "StatAckMessage.h"

StatAckMessage::StatAckMessage(short _messageOpcode, short _age, short _numOfPosts, short _numOfFollowers,
                                     short _numOfFollowing) : AckMessage(_messageOpcode), age(_age),
                                     numOfPosts(_numOfPosts), numOfFollowers(_numOfFollowers), numOfFollowing(_numOfFollowing){}

short StatAckMessage::getAge() {
    return age;
}

short StatAckMessage::getNumOfPosts() {
    return numOfPosts;
}

short StatAckMessage::getNumOfFollowers() {
    return numOfFollowers;
}

short StatAckMessage::getNumOfFollowing() {
    return numOfFollowing;
}
