#include "GameTorrent.h"

GameTorrent::GameTorrent(string p, char mr, string t, int s, string u, int d): Torrent(t, s, u, d), 
    platform(""), maturity_rating(0){
        try {
            if (p == "")
                throw -1;

            if (mr != 'E' && mr != 'M' && mr != 'P')
                throw - 2;

            platform = p; 
            maturity_rating = mr;
        }
        catch (int err_num) {
            if(err_num == -1)
                cout << "Invalid platform!" << endl;

            if(err_num == -2)
                cout << "Invalid maturity rating!" << endl;
        }
    }

GameTorrent::GameTorrent(const GameTorrent& torrent): GameTorrent(torrent.platform, torrent.maturity_rating, torrent.title,
    torrent.size, torrent.uploader, torrent.downloads) {}


char GameTorrent::getMR(void) const{
    return maturity_rating;
}

string GameTorrent::toString(void) const{
    string res;
    res += "Game Torrent: \n";
    res += "    Title: " + title + "\n";
    res += "    Size: " + to_string(size) + "\n";
    res += "    Uploader: " + uploader + "\n";
    res += "    Downloads: " + to_string(downloads) + "\n";
    res += "    Platform: " + platform + "\n";

    string mr = maturity_rating + '\0';
    res += "    Maturity: " + maturity_rating + '\n';
    return res;
}
