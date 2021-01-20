#ifndef TORRENT_SERVER_H
#define TORRENT_SERVER_H
#include "Torrent.h"
#include "game_torrent.h"
#include "film_torrent.h"
#include "software_torrent.h"


class Torrent_server
{
    list<const Torrent*> torrents;
    list<string> user;
public:
    list<const Torrent*> search_name(const string& text);
    list<const game_torrent*> search_mat(char mat);
};
#endif
