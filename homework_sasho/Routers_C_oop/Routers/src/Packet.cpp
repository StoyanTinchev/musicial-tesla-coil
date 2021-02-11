#include "includes/Packet.h"

int Packet::validate(void){
	if (strlen(this->content) == this->content_size)
		return 1;

	return 0;
}

int Packet::num_of_packets = 0;

Packet::Packet(const char* content, const string sender_ip, const string receiver_ip):sender_ip(sender_ip), receiver_ip(receiver_ip){
	this->content_size = strlen(content);
	this->content = new char[this->content_size + 1];
	this->content = content;

	this->num_of_packets ++;
	this->packet_num = this ->num_of_packets;

	if (strlen(content) == 0)
		throw 1;

	if (sender_ip == "0.0.0.0" || sender_ip == "127.0.0.0")
		throw 2;

	if (receiver_ip == "0.0.0.0" || receiver_ip == "127.0.0.0")
		throw 2;
}

Packet::~Packet(){
	delete this->content;
}

const string Packet::getReceiverIP(void) const{
	return this->receiver_ip;
}

const string Packet::getSenderIP(void) const{
	return this->sender_ip;
}

const char* Packet::getContent(void) const{
	return this->content;
}
