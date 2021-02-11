all: FileRead.o Packet.o Router.o main.o
	g++ bin/obj/main.o bin/obj/Packet.o bin/obj/Router.o bin/obj/FileRead.o -o router

FileRead.o: src/FileRead.cpp src/includes/FileRead.h
	g++ -c src/FileRead.cpp -o bin/obj/FileRead.o

Packet.o: src/Packet.cpp src/includes/Packet.h
	g++ -c src/Packet.cpp -o bin/obj/Packet.o

Router.o: src/Router.cpp src/includes/Router.h
	g++ -c src/Router.cpp -o bin/obj/Router.o

main.o: src/main.cpp 
	g++ -c src/main.cpp -o bin/obj/main.o

clear: 
	rm router
	rm bin/obj/*.o
