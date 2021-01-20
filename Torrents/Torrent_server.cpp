#include "Torrent_server.h"


list<const Torrent*> Torrent_server::search_name(const string& text)
{
    try
    {
        list<const Torrent*> search;
        for(auto &it : torrents)
            if(it->get_title().find(text) != string::npos)
                search.push_back(it);

        if(search.empty())
            throw -1;
        else
            return search;
    }
    catch(int)
    {
        cout<< "No matched results!"<<endl;
    }
}

//list<const game_torrent *> Torrent_server::search_mat(char mat)
//{
//    try
//    {
//        list<const game_torrent*> search;
//        for(auto &it : torrents)
//            if(it->get_maturity_rating()==mat)
//                search.push_back(it);
//
//        if(search.empty())
//            throw -1;
//        else
//            return search;
//    }
//    catch(int)
//    {
//        cout<< "No matched results!"<<endl;
//    }
//}



