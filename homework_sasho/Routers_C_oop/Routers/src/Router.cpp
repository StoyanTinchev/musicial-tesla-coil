#include "includes/Router.h"
#include "includes/Packet.h"

bool operator <(routing_info i1, routing_info i2){
	return i1.packets_sent < i2.packets_sent;
}

int Router::query_route(const string ip_addr, const int hop_count){
	if (hop_count <= 1) return 0;

	if (this->ip == ip_addr)
		return 1;

	for(auto& i:(this->routing_table)){
		if (ip_addr == i.ip)
			return 1;
	}

	int router_idx = 0;
	for(auto& i:(this->routers)){
		if(i->query_route(ip_addr, hop_count-1)==1){
			if ((this->routing_table).size() == max_routes){
				auto last = (this->routing_table).end();
				last->ip = ip_addr;
				last->packets_sent = 0;
				last->vect_idx = router_idx;
			}
			else{
				routing_info new_dev;
				new_dev.ip = ip_addr;
				new_dev.packets_sent = 0;
				new_dev.vect_idx = router_idx;

				(this->routing_table).push_back(new_dev);
			}
			return 1;
		}
		router_idx ++;
	}

	return 0;
}

void Router::_sort(void){
	(this->routing_table).sort();
}

Router::Router(string name, string ip){
	this->name = name;
	this->ip = ip;
}

void Router::add_router(Router* new_router){
	try{
		if(new_router->ip == "0.0.0.0" || new_router->ip == "127.0.0.0")
			throw -1;

		(this->routers).push_back(new_router);
	}
	catch(int err_num){
		cout << "Invalid IP!" << endl;
	}
}

void Router::send_package(const Packet& packet){
	try{
		if (packet.getSenderIP() == "0.0.0.0" || packet.getSenderIP() == "127.0.0.0")
			throw -1;

		if (packet.getReceiverIP() == "0.0.0.0" || packet.getReceiverIP() == "127.0.0.0")
			throw -1;

		if (this->ip == packet.getReceiverIP()){
			cout << "Packet sent!" << endl;
			return;
		}

		cout << "Searching for receiver IP in " + this->name + "'s Routing Table..." << endl;

		for(auto &i : (this->routing_table)){
			if(i.ip == packet.getReceiverIP())
			{
				i.packets_sent ++;

				if ((this->routing_table).size() % 10 == 0)
					this->_sort();

				int idx = 0;
				for(auto &r : (this->routers)){
					if(idx == i.vect_idx){
						cout << "Redirecting..." << endl;
						r->send_package(packet);
						return;
					}
					idx++;
				}
			}
		}

		cout << "Receiver IP not found in routing table. Updating..." << endl;

		int stat = this->query_route(packet.getReceiverIP(), max_hops);

		cout << "Searching again for receiver IP in " + this->name + "'s Routing Table..." << endl;
		if (stat){
			for(auto &i : (this->routing_table)){
				if(i.ip == packet.getReceiverIP()){
					i.packets_sent ++;

					if ((this->routing_table).size() % 10 == 0)
						this->_sort();

					int idx = 0;
					for(auto &r : (this->routers)){
						if(idx == i.vect_idx){
							cout << "Redirecting..." << endl;
							r->send_package(packet);
							return;
						}
						idx ++;
					}
				}
			}
			return;
		}

		cout << "Receiver IP nowhere found. Deleting packet..." << endl;
		cout << "Packet Deleted!" << endl;
		return;

	}
	catch(int err_code){
		cout << "Invalid Packet ID!" << endl;
	}

}

string Router::getName(void){
	return this->name;
}

string Router::getIP(void){
	return this->ip;
}
