#ifndef FILEREAD_H_INCLUDED
#define FILEREAD_H_INCLUDED

#include <fstream>
#include "Packet.h"
#include "Router.h"

using namespace std;

void read_routers(vector <Router*>*);
void read_connections(vector <Router*>*);
void read_packets(vector <Packet*>*);

#endif
