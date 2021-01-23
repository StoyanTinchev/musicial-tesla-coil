#pragma once

#include "Torrent.h"

class FilmTorrent : public Torrent
{
    private:
        string director, language;
        unsigned int duration;

    public:
        FilmTorrent(string, int, string, string, int, string, int);
        FilmTorrent(const FilmTorrent&);

        string getDirector(void) const;
        string toString(void) const override;
};