//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_FOLLOWACKMESSAGE_H
#define IDEAPROJECTS_FOLLOWACKMESSAGE_H


#include <string>
#include "AckMessage.h"

class FollowAckMessage : public AckMessage {
private:
    std::string username;
public:
    FollowAckMessage(short _messageOpcode,std::string _username);
    std::string getUsername();


};


#endif //IDEAPROJECTS_FOLLOWACKMESSAGE_H
