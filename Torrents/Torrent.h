#pragma once

#include <iostream>
#include <list>
#include <string>

using namespace std;

class Torrent
{
    protected:
        string title, uploader;
        unsigned int size, downloads;

    public:
        Torrent(string, int, string, int);

        string getTitle(void) const;
        string getUploader(void) const;
        virtual string toString(void) const=0;
};