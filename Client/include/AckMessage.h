//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_ACKMESSAGE_H
#define IDEAPROJECTS_ACKMESSAGE_H


#include "Message.h"

class AckMessage : public Message{

private:
    short messageOpcode;

public:
    AckMessage(short _messageOpcode);
    short getMessageOpcode();


};


#endif //IDEAPROJECTS_ACKMESSAGE_H
