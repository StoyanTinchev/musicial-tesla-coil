#ifndef PACKET_H_INCLUDED
#define PACKET_H_INCLUDED

#include <iostream>
#include <string.h>

using namespace std;

class Packet{
		const char* content;
		unsigned int content_size;
		const string sender_ip;
		const string receiver_ip;
		static int num_of_packets;
		int packet_num;
		int validate(void);

	public:
		Packet(const char*, const string, const string);
		const string getReceiverIP(void) const;
		const string getSenderIP(void) const;
		const char* getContent(void) const;
		~Packet();
};

#endif
