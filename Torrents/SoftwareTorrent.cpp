#include "SoftwareTorrent.h"

SoftwareTorrent::SoftwareTorrent(string dev, string o, int* v, 
    string t, int s, string u, int d) : Torrent(t, s, u, d), developer(dev), os(o) {
        try {
            if (dev == "" || o == "")
                throw -1;

            if (v[0] < 0 || v[1] < 0 || v[2] < 0) {
                throw - 2;
            }

            developer = dev;
            os = o;
            for (int i = 0; i < 3; i++)
                version[i] = v[i];
        }
        catch (int err_num) {
            if (err_num == -1)
                cout << "Invalid OS or Developer name!" << endl;

            if (err_num == -1)
                cout << "Invalid version!" << endl;   
        }
    }

SoftwareTorrent::SoftwareTorrent(const SoftwareTorrent& torrent) :SoftwareTorrent(torrent.developer, torrent.os,
    torrent.version, torrent.title, torrent.size, torrent.uploader, torrent.downloads) {}

string SoftwareTorrent::toString(void) const {
    string res;
    res += "Game Torrent: \n";
    res += "    Title: " + title + "\n";
    res += "    Size: " + to_string(size) + "\n";
    res += "    Uploader: " + uploader + "\n";
    res += "    Downloads: " + to_string(downloads) + "\n";
    res += "    Developer: " + developer + "\n";
    res += "    OS: " + os + "\n";
    res += "    Version: " + to_string(version[0]) + "." + to_string(version[1]) + "." + to_string(version[2]) + "\n";
    return res;
}

unsigned int SoftwareTorrent::getVersion(void) const{
    return version[0];
}
