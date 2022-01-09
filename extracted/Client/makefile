CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread


all: bin/BGSclient


bin/BGSclient: bin/ConnectionHandler.o bin/Client.o bin/AckMessage.o bin/Block.o bin/EncDec.o bin/Error.o bin/FollowAckMessage.o bin/Message.o bin/Notification.o bin/StatAckMessage.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	g++ -Wall -Weffc++ -o bin/BGSclient bin/ConnectionHandler.o bin/Client.o bin/AckMessage.o bin/Block.o bin/EncDec.o bin/Error.o bin/FollowAckMessage.o bin/Message.o bin/Notification.o bin/StatAckMessage.o $(LDFLAGS)
	@echo 'Finished building target: BGSclient'

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/AckMessage.o: src/AckMessage.cpp
	g++ $(CFLAGS) -o bin/AckMessage.o src/AckMessage.cpp

bin/Block.o: src/Block.cpp
	g++ $(CFLAGS) -o bin/Block.o src/Block.cpp

bin/EncDec.o: src/EncDec.cpp
	g++ $(CFLAGS) -o bin/EncDec.o src/EncDec.cpp

bin/Error.o: src/Error.cpp
	g++ $(CFLAGS) -o bin/Error.o src/Error.cpp

bin/FollowAckMessage.o: src/FollowAckMessage.cpp
	g++ $(CFLAGS) -o bin/FollowAckMessage.o src/FollowAckMessage.cpp

bin/Message.o: src/Message.cpp
	g++ $(CFLAGS) -o bin/Message.o src/Message.cpp

bin/Notification.o: src/Notification.cpp
	g++ $(CFLAGS) -o bin/Notification.o src/Notification.cpp

bin/StatAckMessage.o: src/StatAckMessage.cpp
	g++ $(CFLAGS) -o bin/StatAckMessage.o src/StatAckMessage.cpp

clean:
	rm -f bin/*
