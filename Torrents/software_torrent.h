#ifndef SOFTWARE_TORRENT_H
#define SOFTWARE_TORRENT_H
#include "Torrent.h"

class software_torrent : public Torrent
{
    string creator;
    string os;
    unsigned int version[3];
public:
    software_torrent(string creator, string os, unsigned int *version, string Title,
                     unsigned int size, string uploadded_by, unsigned int downloads);

    software_torrent(const software_torrent &torrent);

    string toString() const override;
};
#endif
