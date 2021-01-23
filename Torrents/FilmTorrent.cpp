#include "FilmTorrent.h"

FilmTorrent::FilmTorrent(string dir, int dur, string l, string t,
    int s, string u, int d) : Torrent(t, s, u, d), director(""), duration(0), language("") {
    try {
        if (dir == "" || l == "") 
            throw -1;

        if (dur < 1)
            throw -2;

        director = dir;
        duration = dur;
        language = l;
    }
    catch (int err_num) {
        if(err_num == -1)
            cout << "Invalid director name and language!" << endl;

        if(err_num == -2)
            cout << "Invalid duration!" << endl;
    }
}

FilmTorrent::FilmTorrent(const FilmTorrent& torrent): FilmTorrent(torrent.director, torrent.duration,
    torrent.language, torrent.title, torrent.size, torrent.uploader, torrent.downloads){}

string FilmTorrent::toString(void) const {
    string res;
    res += "Film Torrent: \n";
    res += "    Title: " + title + "\n";
    res += "    Size: " + to_string(size) + "\n";
    res += "    Uploader: " + uploader + "\n";
    res += "    Downloads: " + to_string(downloads) + "\n";
    res += "    Director: " + director + "\n";
    res += "    Duration: " + to_string(duration) + "\n";
    res += "    Language: " + language + "\n";
    return res;
}

string FilmTorrent::getDirector(void) const{
    return director;
}
