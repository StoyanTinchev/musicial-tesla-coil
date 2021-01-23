#ifndef TORRENT_H
#define TORRENT_H
#include <iostream>
#include <list>
#include <utility>
#include <sstream>


using namespace std;

class Torrent
{
protected:
    const string Title;
    const unsigned int size;
    const string uploaded_by;
    const unsigned int downloads;
public:
    Torrent(string Title, unsigned int size, string uploadded_by, unsigned int downloads);

    Torrent(const Torrent &torrent);

    string get_title() const;
    string get_uploaded_by() const;
    virtual string toString() const;
};
#endif