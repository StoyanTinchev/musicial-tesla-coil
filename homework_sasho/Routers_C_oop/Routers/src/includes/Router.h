#ifndef ROUTER_H_INCLUDED
#define ROUTER_H_INCLUDED

#include <iostream>
#include <vector>
#include <list>
#include "Packet.h"

using namespace std;

typedef struct{
	string ip;
	int vect_idx;
	int packets_sent;
}routing_info;

class Router{
		string name;
		string ip;
		vector <Router*> routers;
		list <routing_info> routing_table;
		const static int max_routes = 10;
		const static int max_hops = 10;
		int query_route(const string, const int);
		void _sort(void);

	public:
		Router(string, string);
		void add_router(Router*);
		string getName(void);
		string getIP(void);
		void send_package(const Packet&);
};

bool operator <(routing_info, routing_info);

#endif
