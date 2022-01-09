//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_ERROR_H
#define IDEAPROJECTS_ERROR_H

#include "Message.h"

class Error : public Message {
private:
    short messageOpcode;

public:
    Error(short _messageOpcode);
    short getMessageOpcode();
    std::string toString();

};


#endif //IDEAPROJECTS_ERROR_H
