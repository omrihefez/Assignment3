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



std::string EncDec::encode(std::string toEncode) {
    std::string output = "";
    short opcode = -1;
    if (toEncode.substr(0, 8) == "REGISTER"){
        opcode = 1;
        output += "01";
    }
    else if (toEncode.substr(0, 5) == "LOGIN"){
        opcode = 2;
        output += "02";
    }
    else if (toEncode.substr(0, 6) == "LOGOUT"){
        opcode = 3;
        output += "03";
    }
    else if (toEncode.substr(0, 6) == "FOLLOW"){
        opcode = 4;
        output += "04";
    }
    else if (toEncode.substr(0, 4) == "POST"){
        opcode = 5;
        output += "05";
    }
    else if (toEncode.substr(0, 2) == "PM"){
        opcode = 6;
        output += "06";
    }
    else if (toEncode.substr(0, 7) == "LOGSTAT"){
        opcode = 7;
        output += "07";
    }
    else if (toEncode.substr(0, 4) == "STAT"){
        opcode = 8;
        output += "08";
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
            break;
        }
        case 3: {
            break;
        }
        case 4: {
            size_t index = toEncode.find_first_of(' ') + 1;
            output += toEncode[index++];
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
            char time_buf[16];
            time_t now;
            time(&now);
            strftime(time_buf, 16, "%d-%m-%Y %H:%M", gmtime(&now));
            std::string time = time_buf;
            output += username + '\0' + content + '\0' + time + '\0';
            break;
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