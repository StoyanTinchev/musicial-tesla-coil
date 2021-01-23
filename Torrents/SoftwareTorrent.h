#pragma once

#include "Torrent.h"

class SoftwareTorrent : public Torrent
{
    private:
        string developer, os;
        int* version;

    public:
        SoftwareTorrent(string, string, int*, string, int, string, int);
        SoftwareTorrent(const SoftwareTorrent&);

        unsigned int getVersion(void) const;
        string toString(void) const override;
};
