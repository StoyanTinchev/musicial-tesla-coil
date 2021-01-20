#include <iostream>
#include <list>
#include <utility>
#include <sstream>


using namespace std;

class Torrent
{
protected:
    string Title;
    unsigned int size;
    string uploaded_by;
    unsigned int downloads;
public:
    Torrent(string Title, unsigned int size, string uploadded_by, unsigned int downloads) :
    Title(std::move(Title)), size(size),
    uploaded_by(std::move(uploadded_by)), downloads(downloads){}

    Torrent(const Torrent &torrent) : Title(torrent.Title), size(torrent.size),
    uploaded_by(torrent.uploaded_by), downloads(torrent.downloads){}


    virtual string toString()
    {
        stringstream ss;
        ss << Title << ", " << size << ", " << uploaded_by << ", " << downloads;
        string ret;
        ss >> ret;
        return ret;
    }
};
class game_torrent : public Torrent
{
    string platform;
    char maturity_rating;
public:
    game_torrent(string platform, char maturaty_rating, string Title,
                 unsigned int size, string uploadded_by, unsigned int downloads) :
    platform(std::move(platform)), maturity_rating(maturaty_rating),
    Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

    game_torrent(const game_torrent &torrent) : platform(torrent.platform),
    maturity_rating(torrent.maturity_rating),
    Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads) {}

    string toString() override
    {
        stringstream ss;
        ss << Title << ", " << size << ", " << uploaded_by << ", " << downloads;
        ss << ", " << platform << ", " << maturity_rating;
        string ret;
        ss >> ret;
        return ret;
    }
};
class film_torrent : public Torrent
{
    string director;
    unsigned int duration;
    string language;
public:
    film_torrent(string director, unsigned int duration, string language, string Title,
    unsigned int size, string uploadded_by, unsigned int downloads) :
    director(std::move(director)), duration(duration), language(std::move(language)),
    Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

    film_torrent(const film_torrent &torrent) : director(torrent.director),
    duration(torrent.duration), language(torrent.language),
    Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads) {}


    string toString() override
    {
        stringstream ss;
        ss << Title << ", " << size << ", " << uploaded_by << ", " << downloads;
        ss << ", " << director << ", " << duration << ", " << language;
        string ret;
        ss >> ret;
        return ret;
    }
};
class software_torrent : public Torrent
{
    string creator;
    string os;
    unsigned int version[3];
public:
    software_torrent(string creator, string os, unsigned int *version, string Title,
                     unsigned int size, string uploadded_by, unsigned int downloads) :
    creator(std::move(creator)), os(std::move(os)), version{version[0], version[1], version[2]},
    Torrent(std::move(Title), size, std::move(uploadded_by), downloads) {}

    software_torrent(const software_torrent &torrent) : creator(torrent.creator),
    os(torrent.os), version{torrent.version[0], torrent.version[1], torrent.version[2]},
    Torrent(torrent.Title, torrent.size, torrent.uploaded_by, torrent.downloads){}



    string toString() override
    {
        stringstream ss;
        ss << Title << ", " << size << ", " << uploaded_by << ", " << downloads;
        ss << ", " << creator << ", " << os << ", " << version[0] << "." << version[1] << "." << version[2];
        string ret;
        ss >> ret;
        return ret;
    }
};
class Torrent_server
{
    list<Torrent> torrents;
    list<string> user;
};
int main()
{
    return 0;
}