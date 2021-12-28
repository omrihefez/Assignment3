#include <connectionHandler.h>
#include <Message.h>
#include <AckMessage.h>
#include <FollowAckMessage.h>
#include "StatAckMessage.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}

short bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void shortToBytes(short num, char* bytesArr){
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

short ConnectionHandler::getShort(){
    short output = -1;
    char bytes[2] = {};
    if (ConnectionHandler::getBytes(bytes, 2)){
        output = ConnectionHandler::bytesToShort(bytes);
    }
    return output;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\n');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\n');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

Message ConnectionHandler::getMessage(short opcode) {
    Message output;
    switch (opcode) {
        case 9: {
            short messageOpcode = getShort();
            switch (messageOpcode) {
                case 1 : {
                    output = AckMessage(1);
                    break;
                }
                case 2: {
                    output = AckMessage(2);
                    break;
                }
                case 3: {
                    output = AckMessage(3);
                    break;
                }
                case 4: {
                    std::string username = ConnectionHandler::getString();
                    output = FollowAckMessage(4, username);
                    break;
                }
                case 5: {
                    output = AckMessage(5);
                    break;
                }
                case 6: {
                    output = AckMessage(6);
                    break;
                }
                case 7: {
                    short age = getShort();
                    short numOfPosts = getShort();
                    short numOfFollowers = getShort();
                    short numOfFollowing = getShort();
                    output = StatAckMessage(7, age, numOfPosts, numOfFollowers, numOfFollowing);
                    break;
                }
                case 8: {
                    short age = getShort();
                    short numOfPosts = getShort();
                    short numOfFollowers = getShort();
                    short numOfFollowing = getShort();
                    output = StatAckMessage(8, age, numOfPosts, numOfFollowers, numOfFollowing);
                    break;
                }
            }
            break;
        }
        case 10: {
            short messageOpcode = getShort();

            break;
        }

        case 11:{

            break;
        }

        case 12:{

            break;
        }
    }
    return output;
}

std::string ConnectionHandler::getString() {
    string output = "";
    output = getFrameAscii(output, '\0');
    return output;

}
