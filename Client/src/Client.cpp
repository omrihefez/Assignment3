#include <stdlib.h>
#include <ConnectionHandler.h>
#include <EncDec.h>
#include <AckMessage.h>
#include <thread>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

bool terminate = false;

void readFromServer(ConnectionHandler& connectionHandler) {
    while (!terminate) {
        Message *m;
        short opcode;
        try {
            opcode = connectionHandler.getShort();
            m = connectionHandler.getMessage(opcode);
            std::cout << m->toString() << std::endl;
            if (dynamic_cast<AckMessage*>(m) != nullptr)
                if ((static_cast<AckMessage *>(m))->getMessageOpcode() == 3) {
                    terminate = true;
                    std::cout << "Client logged out, goodbye!" << std::endl;
                    delete m;
                    exit(0);
                }
            delete m;
        } catch (std::exception &e) {}
    }
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    EncDec encdec;

    std::thread read(readFromServer, std::ref(connectionHandler));
	
	//From here we will see the rest of the ehco client implementation:
    while (!terminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        std::string encoded = encdec.encode(line);
        int len = encoded.length();
        if (!connectionHandler.sendLine(encoded)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefore, we send len+1 bytes.
        std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
    }
 
//        // We can use one of three options to read data from the server:
//        // 1. Read a fixed number of characters
//        // 2. Read a line (up to the newline character using the getline() buffered reader
//        // 3. Read up to the null character
//        std::string answer;
//        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
//        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
//        if (!connectionHandler.getLine(answer)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }
//
//		len=answer.length();
//		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
//		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
//        answer.resize(len-1);
//        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
//        if (answer == "bye") {
//            std::cout << "Exiting...\n" << std::endl;
//            break;
//        }
//    }
    return 0;
}
