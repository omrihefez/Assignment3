//
// Created by Omri on 28/12/2021.
//

#include <FollowAckMessage.h>

FollowAckMessage::FollowAckMessage(short _messageOpcode, std::string _username): AckMessage(_messageOpcode), username(_username) {}

std::string FollowAckMessage::getUsername() {
    return username;
}