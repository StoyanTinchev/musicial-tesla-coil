main: main.o Torrent.o game_torrent.o film_torrent.o software_torrent.o Torrent_server.o
	g++ -o run main.o Torrent.o game_torrent.o film_torrent.o software_torrent.o Torrent_server.o

main.o: main.cpp
	g++ -c main.cpp

Torrent.o: Torrent.cpp Torrent.h
	g++ -c Torrent.cpp

game_torrent.o: game_torrent.cpp game_torrent.h
	g++ -c game_torrent.cpp

film_torrent.o: film_torrent.cpp film_torrent.h
	g++ -c film_torrent.cpp

software_torrent.o: software_torrent.cpp software_torrent.h
	g++ -c software_torrent.cpp

Torrent_server.o: Torrent_server.cpp Torrent_server.h
	g++ -c Torrent_server.cpp

clean:
	-rm *.o
	-rm run