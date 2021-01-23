#ifndef FILM_TORRENT_H
#define FILM_TORRENT_H
#include "Torrent.h"

class film_torrent : public Torrent
{
    const string director;
    const unsigned int duration;
    const string language;
public:
    film_torrent(string director, unsigned int duration, string language, string Title,
                 unsigned int size, string uploadded_by, unsigned int downloads);

    film_torrent(const film_torrent &torrent);

    string get_director_name() const;
    string toString() const override;
};
#endif
