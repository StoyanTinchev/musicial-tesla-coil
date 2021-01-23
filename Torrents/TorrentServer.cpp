#include "TorrentServer.h"

#include <utility>

Torrent_server::Torrent_server(list<string> users) : users(std::move(users)){}


void Torrent_server::add_torrent(const Torrent *torrent)
{
    try
    {
        bool flag = false;
        for(auto &it : users)
            if(it == torrent->getUploader())
            {
                flag = true;
                torrents.push_back(torrent);
            }
        if(!flag)
            throw -1;
    }
    catch (int)
    {
        cout<< "Uploaded name does not exists!"<<endl;
    }
}


list<const Torrent*> Torrent_server::search_name(const string& text)
{
    try
    {
        list<const Torrent*> search;
        for(auto &it : torrents)
            if(it->getTitle().find(text) != string::npos)
                search.push_back(it);

        if(search.empty())
            throw -1;
        else
            return search;
    }
    catch(int)
    {
        cout<< "There is no such text in any title!"<<endl;
    }
}

list<const GameTorrent *> Torrent_server::search_mat(const char mat)
{
    try
    {
        list<const GameTorrent*> search;
        for(auto &it : torrents)
            if(dynamic_cast<const GameTorrent*>(it)!=nullptr)
                search.push_back(dynamic_cast<const GameTorrent*>(it));

        for(auto &it : search)
            if(it->get_maturity_rating()!=mat)
                search.remove(it);

        if(search.empty())
            throw -1;
        else
            return search;
    }
    catch(int)
    {
        cout<< "No maturity rating found!"<<endl;
    }
}

list<const film_torrent *> Torrent_server::serach_director(const string& director)
{
    try
    {
        list<const film_torrent*> search;
        for(auto &it : torrents)
            if(dynamic_cast<const film_torrent*>(it)!=nullptr)
                search.push_back(dynamic_cast<const film_torrent*>(it));

        for(auto &it : search)
            if(it->get_director_name().find(director) == string::npos)
                search.remove(it);

        if(search.empty())
            throw -1;
        else
            return search;
    }
    catch(int)
    {
        cout<< "No such a director!"<<endl;
    }
}

list<const software_torrent *> Torrent_server::search_major_version(const int major)
{
    try
    {
        list<const software_torrent*> search;
        for(auto &it : torrents)
            if(dynamic_cast<const software_torrent*>(it)!=nullptr)
                search.push_back(dynamic_cast<const software_torrent*>(it));

        for(auto &it : search)
            if(it->get_major_version() != major)
                search.remove(it);

        if(search.empty())
            throw -1;
        else
            return search;
    }
    catch(int)
    {
        cout<< "No such a major version!"<<endl;
    }
}





