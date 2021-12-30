//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_MESSAGE_H
#define IDEAPROJECTS_MESSAGE_H


#include <string>

class Message  {

public:
    virtual std::string toString() = 0;
    virtual ~Message();

};


#endif //IDEAPROJECTS_MESSAGE_H
