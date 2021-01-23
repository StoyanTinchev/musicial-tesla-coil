#include "Torrent.h"



Torrent::Torrent(string Title, unsigned int size, string uploadded_by, unsigned int downloads) :
        Title(std::move(Title)), size(size),
        uploaded_by(std::move(uploadded_by)), downloads(downloads){}

Torrent::Torrent(const Torrent &torrent) : Title(torrent.Title), size(torrent.size),
                                  uploaded_by(torrent.uploaded_by), downloads(torrent.downloads){}

string Torrent::get_title() const{
    return this->Title;
}

string Torrent::get_uploaded_by() const
{
    return this->uploaded_by;
}

string Torrent::toString() const {
    stringstream ss;
    ss << "Title: " << Title << ", "
    <<"   Size: "<< size << ", "
    <<"   uploaded_by: " << uploaded_by << ", "
    <<"   downloads: " << downloads << endl;
    string ret;
    ss >> ret;
    return ret;
}
