#ifndef TORRENT_SERVER_H
#define TORRENT_SERVER_H
#include "Torrent.h"
#include "game_torrent.h"
#include "film_torrent.h"
#include "software_torrent.h"


class Torrent_server
{
    list<const Torrent*> torrents;
    list<string> users;
public:
    Torrent_server(list<string> users);
    void add_torrent(const Torrent* torrent);
    list<const Torrent*> search_name(const string& text);
    list<const game_torrent*> search_mat(char mat);
    list<const film_torrent*> serach_director(const string& director);
    list<const software_torrent*> search_major_version(int major);
};
#endif
