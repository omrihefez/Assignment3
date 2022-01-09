#include <ConnectionHandler.h>
#include <Message.h>
#include <AckMessage.h>
#include <FollowAckMessage.h>
#include <Notification.h>
#include <Error.h>
#include <Block.h>
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

short ConnectionHandler::bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr){
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
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
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

Message* ConnectionHandler::getMessage(short opcode) {
    Message* output;
    switch (opcode) {
        case 9: {
            char ch;
            getBytes(&ch, 1);
            Type type = ch == '0' ? PM : Public;
            std::string postingUser = getString();
            std::string content = getString();
            output = new Notification(type,postingUser,content);
            std::string s = "";
            ConnectionHandler::getLine(s);
            break;
        }
        case 10: {
            short messageOpcode = getShort();
            switch (messageOpcode) {
                case 1 : {
                    output = new AckMessage(1);
                    break;
                }
                case 2: {
                    output = new AckMessage(2);
                    break;
                }
                case 3: {
                    output = new AckMessage(3);
                    break;
                }
                case 4: {
                    std::string username = ConnectionHandler::getString();
                    if (username[username.size()-1] == '\0')
                        username = username.substr(0, username.size()-1);
                    output = new FollowAckMessage(4, username);
                    break;
                }
                case 5: {
                    output = new AckMessage(5);
                    break;
                }
                case 6: {
                    output = new AckMessage(6);
                    break;
                }
                case 7: {
                    short age = getShort();
                    short numOfPosts = getShort();
                    short numOfFollowers = getShort();
                    short numOfFollowing = getShort();
                    output = new StatAckMessage(7, age, numOfPosts, numOfFollowers, numOfFollowing);
                    break;
                }
                case 8: {
                    short age = getShort();
                    short numOfPosts = getShort();
                    short numOfFollowers = getShort();
                    short numOfFollowing = getShort();
                    output = new StatAckMessage(8, age, numOfPosts, numOfFollowers, numOfFollowing);
                    break;
                }
                case 12 : {
                    output = new AckMessage(12);
                    break;
                }
            }
            std::string s = "";
            ConnectionHandler::getLine(s);
            break;
        }
        case 11: {
            short messageOpcode = getShort();
            output = new Error(messageOpcode);
            std::string s = "";
            ConnectionHandler::getLine(s);
            break;
        }
    }
    return output;
}

std::string ConnectionHandler::getString() {
    std::string output = "";
    getFrameAscii(output, '\0');
    return output;

}
