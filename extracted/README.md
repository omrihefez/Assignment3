# Assignment3
Maor Zamsky 318595915
Omri Hefez 318383056

How to run the code:

Server:
    Reactor:
        cd Server
        mvn clean
        mvn package
        cd target
        java -cp spl-net-1.0-SNAPSHOT.jar bgu.spl.net.srv.Reactor <numOfThreads> <port>

    TPC:
        cd Server
        mvn clean
        mvn package
        cd target
        java -cp spl-net-1.0-SNAPSHOT.jar bgu.spl.net.srv.TcpServer <port>

Client:
    cd Client
    make clean
    make
    bin/BGSclient <IP(localhost)> <port>


Message Example:
    REGISTER omri 1234 01-01-2000

    LOGIN omri 1234 1

    LOGOUT

    FOLLOW 0 maor

    FOLLOW 1 maor

    POST How you doing?

    PM maor Hi how are you?

    LOGSTAT

    STAT maor|omri

    BLOCK maor

Filter Words:
    TCP:
        stored in BaseServer (line 40)

    Reactor:
        stored in Reactor (line 56)