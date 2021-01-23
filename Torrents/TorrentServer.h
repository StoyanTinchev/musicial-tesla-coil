#pragma once
#include "Torrent.h"
#include "GameTorrent.h"
#include "FilmTorrent.h"
#include "SoftwareTorrent.h"


class Torrent_server
{
    list<const Torrent*> torrents;
    list<string> users;
public:
    Torrent_server(list<string> users);
    void add_torrent(const Torrent* torrent);
    list<const Torrent*> search_name(const string& text);
    list<const GameTorrent*> search_mat(char mat);
    list<const FilmTorrent*> serach_director(const string& director);
    list<const SoftwareTorrent*> search_major_version(int major);
};
