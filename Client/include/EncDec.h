//
// Created by Omri on 30/12/2021.
//

#ifndef IDEAPROJECTS_ENCDEC_H
#define IDEAPROJECTS_ENCDEC_H


#include <string>
#include "Message.h"

class EncDec {

public:
    std::string encode(std::string toEncode);
    std::string shortToBytes(short num);

};


#endif //IDEAPROJECTS_ENCDEC_H
