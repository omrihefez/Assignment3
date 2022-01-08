//
// Created by Omri on 30/12/2021.
//

#include <EncDec.h>
#include <Notification.h>
#include <ConnectionHandler.h>
#include <AckMessage.h>
#include <FollowAckMessage.h>
#include <Error.h>
#include <Block.h>
#include <iomanip>
#include "StatAckMessage.h"

std::string EncDec::shortToBytes(short num){
    std::string bytesArr;
    bytesArr.append(1,(char) ((num >> 8) & 0xFF));
    bytesArr.append(1,(char)(num & 0xFF));
    return bytesArr;
}

//void EncDec::shortToBytes(short num, char* bytesArr) {
//    bytesArr[0] = ((num >> 8) & 0xFF);
//    bytesArr[1] = (num & 0xFF);
//}



std::string EncDec::encode(std::string toEncode) {
    std::string output = "";
    short opcode = -1;
    if (toEncode.substr(0, 8) == "REGISTER"){
        opcode = 1;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 5) == "LOGIN"){
        opcode = 2;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 6) == "LOGOUT"){
        opcode = 3;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 6) == "FOLLOW"){
        opcode = 4;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 4) == "POST"){
        opcode = 5;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 2) == "PM"){
        opcode = 6;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 7) == "LOGSTAT"){
        opcode = 7;
        output += shortToBytes(opcode);
    }
    else if (toEncode.substr(0, 4) == "STAT"){
        opcode = 8;
        output += shortToBytes(opcode);
    }
    switch (opcode) {
        case 1: {
            size_t index = toEncode.find_first_of(' ') + 1;
            while (index < toEncode.length()) {
                if (toEncode[index] == ' ')
                    output += '\0';
                else
                    output += toEncode[index];
                index++;
            }
            output += '\0';
            break;
        }
        case 2: {
            size_t index = toEncode.find_first_of(' ') + 1;
            while (index < toEncode.length()) {
                if (toEncode[index] == ' ')
                    output += '\0';
                else
                    output += toEncode[index];
                index++;
            }
            output += '\0';
            break;
        }
        case 3: {
            break;
        }
        case 4: {
            size_t index = toEncode.find_first_of(' ') + 1;
            output += toEncode[index++];
            index++;
            while (index < toEncode.length()) {
                if (toEncode[index] != ';')
                    output += toEncode[index];
                index++;
            }
            output += '\0';
            break;
        }
        case 5: {
            size_t index = toEncode.find_first_of(' ') + 1;
            while (index < toEncode.length()) {
                output += toEncode[index];
                index++;
            }
            output += '\0';
            break;
        }
        case 6: {
            size_t index = toEncode.find_first_of(' ') + 1;
            std::string username = "";
            std::string content = "";
            while (toEncode[index] != ' ') {
                username += toEncode[index];
                index++;
            }
            index++;
            while (index < toEncode.length()) {
                content += toEncode[index];
                index++;
            }
            time_t rawtime;
            struct tm * timeinfo;
            char buffer[80];
            time (&rawtime);
            timeinfo = localtime(&rawtime);
            strftime(buffer,sizeof(buffer),"%d-%m-%Y %H:%M",timeinfo);
            std::string str(buffer);
            output += str + '\0';
        }
        case 7: {
            break;
        }
        case 8: {
            size_t index = toEncode.find_first_of(' ') + 1;
            while (index < toEncode.length()) {
                output += toEncode[index];
                index++;
            }
            output += '\0';
            break;
        }
    }
    return output;
}