//
// Created by Omri on 28/12/2021.
//

#ifndef IDEAPROJECTS_NOTIFICATION_H
#define IDEAPROJECTS_NOTIFICATION_H


#include <string>
#include "Message.h"

enum Type {PM, Public};

class Notification: public Message {
private:
    Type type;
    std::string postingUser;
    std::string content;

public:
    Notification(Type _type, std::string _postingUser, std::string _content);
    Type getType();
    std::string getPostingUser();
    std::string getContent();
};


#endif //IDEAPROJECTS_NOTIFICATION_H
