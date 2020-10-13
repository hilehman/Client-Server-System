# define some Makefile variables for the compiler and compiler flags
# to use Makefile variables later in the Makefile: $()
CFLAGS:= -c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

# All Targets
all: Client

# Tool invocations
# Executable "Main"
Client: bin/fromKB.o bin/ClientData.o bin/fromServer.o bin/connectionHandler.o bin/Client.o

	g++ -o bin/StompBookClubClient bin/fromKB.o bin/ClientData.o bin/fromServer.o bin/connectionHandler.o bin/Client.o $(LDFLAGS)




# Depends on the source and header files
bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/ClientData.o: src/ClientData.cpp
	g++ $(CFLAGS) -o bin/ClientData.o src/ClientData.cpp

bin/fromKB.o: src/fromKB.cpp
	g++ $(CFLAGS) -o bin/fromKB.o src/fromKB.cpp

bin/fromServer.o: src/fromServer.cpp
	g++ $(CFLAGS) -o bin/fromServer.o src/fromServer.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp


#Clean the build directory
clean:
	rm -f bin/*
