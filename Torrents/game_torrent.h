#ifndef GAME_TORRENT_H
#define GAME_TORRENT_H
#include "Torrent.h"

class game_torrent : public Torrent
{
    const string platform;
    const char maturity_rating;
public:
    game_torrent(string platform, char maturaty_rating, string Title,
                 unsigned int size, string uploadded_by, unsigned int downloads);

    game_torrent(const game_torrent &torrent);

    char get_maturity_rating() const;
    string toString() const override;
};
#endif
