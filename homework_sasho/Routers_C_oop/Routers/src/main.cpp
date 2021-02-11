#include "includes/Packet.h"
#include "includes/Router.h"
#include "includes/FileRead.h"

int main(){
	vector <Router*> routers;
	vector <Packet*> packets;

	read_packets(&packets);
	read_routers(&routers);
	read_connections(&routers);

	for (auto &p : packets){
		for (auto &r : routers){
			if (p->getSenderIP() == r->getIP()){
				r->send_package(*p);
				cout << endl;
			}
        }
    }

    return 0;
}
