#include "game_torrent.h"


game_torrent::game_torrent(string platform, char maturaty_rating, string Title,
             unsigned int size, string uploadded_by, unsigned int downloads) :
        platform(std::move(platform)), maturity_rating(maturaty_rating),
        Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

game_torrent::game_torrent(const game_torrent &torrent) : platform(torrent.platform),
                                            maturity_rating(torrent.maturity_rating),
                                            Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads) {}


char game_torrent::get_maturity_rating() const
{
    return this->maturity_rating;
}

string game_torrent::toString() const
{
    stringstream ss;
    ss << Torrent::toString()
    << "Platform: " << platform << ", "
    << "   Maturity rating: " << maturity_rating;
    string ret;
    ss >> ret;
    return ret;
}
