#include "film_torrent.h"


film_torrent::film_torrent(string director, unsigned int duration, string language, string Title,
             unsigned int size, string uploadded_by, unsigned int downloads) :
        director(std::move(director)), duration(duration), language(std::move(language)),
        Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

film_torrent::film_torrent(const film_torrent &torrent) : director(torrent.director),
                                            duration(torrent.duration), language(torrent.language),
                                            Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads) {}


string film_torrent::toString() const
{
    stringstream ss;
    ss << Title << ", " << size << ", " << uploaded_by << ", " << downloads;
    ss << ", " << director << ", " << duration << ", " << language;
    string ret;
    ss >> ret;
    return ret;
}