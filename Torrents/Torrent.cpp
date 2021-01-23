#include "Torrent.h"

Torrent::Torrent(string t, int s, string u, int d):size(0), downloads(0), title(""), uploader(""){
    try {
        if (t == "" || u == "")
            throw -1;

        if (s < 1 || d < 0)
            throw -2;

        title = t;
        size = s;
        uploader = u;
        downloads = d;
    }
    catch (int err_num) {
        if (err_num == -1)
            cout << "Invalid name or title!" << endl;

        if (err_num == -2)
            cout << "Invalid size or downloads!" << endl;
    }
}

string Torrent::getTitle(void) const {
    return title;
}

string Torrent::getUploader(void) const {
    return uploader;
}