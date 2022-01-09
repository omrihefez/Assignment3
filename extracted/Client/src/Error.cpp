//
// Created by Omri on 28/12/2021.
//

#include <Error.h>

Error::Error(short _messageOpcode): Message(), messageOpcode(_messageOpcode) {}

short Error::getMessageOpcode() {
    return messageOpcode;
}

std::string Error::toString() {
    return "Error " + std::to_string(getMessageOpcode());
}
