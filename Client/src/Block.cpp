//
// Created by Omri on 28/12/2021.
//

#include <Block.h>

Block::Block(std::string _username): Message(), username(_username) {}

std::string Block::getUsername() {
    return username;
}

std::string Block::toString() {
    return "Block " + getUsername();
}
