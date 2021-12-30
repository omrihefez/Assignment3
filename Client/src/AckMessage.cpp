//
// Created by Omri on 28/12/2021.
//

#include <AckMessage.h>

AckMessage::AckMessage(short _messageOpcode): messageOpcode(_messageOpcode){}

short AckMessage::getMessageOpcode() {
    return messageOpcode;
}

std::string AckMessage::toString() {
    return "Ack " + std::to_string(getMessageOpcode());
}
