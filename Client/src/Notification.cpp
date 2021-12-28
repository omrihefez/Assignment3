//
// Created by Omri on 28/12/2021.
//

#include <Notification.h>


Notification::Notification(Type _type, std::string _postingUser, std::string _content)
    : Message(), type(_type), postingUser(_postingUser), content(_content)  {}

Type Notification::getType() {
    return type;
}

std::string Notification::getPostingUser() {
    return postingUser;
}

std::string Notification::getContent() {
    return content;
}
