#include "includes/FileRead.h"

void read_routers(vector <Router*>* routers){
	ifstream routers_file = ifstream("txt/routers.txt");

	string name, ip, info;

	if(routers_file.is_open()){
        for (int in = 0; routers_file >> info; in ++){
            if(in % 2 == 0)
                name = info;
			else{
                ip = info;

                Router* new_router = new Router(name, ip);
                routers->push_back(new_router);
            }
        }
        routers_file.close();
    }
}

void read_connections(vector <Router*>* routers){
	ifstream network_file = ifstream("txt/network.txt");

	string info;
	Router* first_r;
	Router* second_r;

	if(network_file.is_open()){
        for (int in = 0; network_file >> info; in ++){
            if(in % 2 == 0){
                for(auto r : *routers)
                    if(r->getName() == info)
						first_r = r;
            }
            else{
                for(auto &r : *routers)
                    if(r->getName() == info)
						second_r = r;


                first_r->add_router(second_r);
                second_r->add_router(first_r);
            }
        }
        network_file.close();
    }
}

void read_packets(vector <Packet*>* packets){
	ifstream packets_file = ifstream("txt/package.txt");

	string info, source_ip, target_ip, content;

	if (packets_file.is_open()){
		for (int in = 0; packets_file >> info; in ++){
			switch(in % 3){
				case 0:
					source_ip = info;
					break;

				case 1:
					target_ip = info;
					break;

				case 2:
					content = info;
					int len = content.size();

					char* buff = new char[len];

					ssize_t l = content.copy(buff, len-2, 1);
					buff[l] = '\0';

					try{
						Packet* new_packet = new Packet(buff, source_ip, target_ip);
						packets->push_back(new_packet);
					}
					catch(int err_code){
						switch(err_code){
							case 1:
								cout << "Empty packet content!" << endl;
								break;

							case 2:
								cout << "Invalid IP!" << endl;
								break;
						}
						cout << endl;
					}
					break;
			}
		}
		packets_file.close();
	}
}
