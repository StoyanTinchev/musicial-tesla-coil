#pragma once

#include "Torrent.h"

class GameTorrent : public Torrent
{
    private:
        string platform;
        char maturity_rating;

    public:
        GameTorrent(string, char, string, int, string, int);
        GameTorrent(const GameTorrent& torrent);

        char getMR(void) const;
        string toString(void) const override;
};

