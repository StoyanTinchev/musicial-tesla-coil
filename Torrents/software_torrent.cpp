#include "software_torrent.h"


software_torrent::software_torrent(string creator, string os, unsigned int *version, string Title,
                 unsigned int size, string uploadded_by, unsigned int downloads) :
        creator(std::move(creator)), os(std::move(os)), version{version[0], version[1], version[2]},
        Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

software_torrent::software_torrent(const software_torrent &torrent) : creator(torrent.creator),
                                                    os(torrent.os), version{torrent.version[0], torrent.version[1], torrent.version[2]},
                                                    Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads){}



string software_torrent::toString() const
{
    stringstream ss;
    ss << Torrent::toString()
    << "Creator: " << creator << ", "
    << "   OS: " << os << ", "
    << "   Version: " << version[0] << "." << version[1] << "." << version[2];
    string ret;
    ss >> ret;
    return ret;
}

unsigned int software_torrent::get_major_version() const
{
    return this->version[0];
}
