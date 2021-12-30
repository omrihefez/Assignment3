//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_BLOCK_H
#define IDEAPROJECTS_BLOCK_H

#include <string>
#include "Message.h"


class Block : public Message {
private:
    std::string username;

public:
    Block(std::string _username);
    std::string getUsername();
    std::string toString();

};


#endif //IDEAPROJECTS_BLOCK_H
