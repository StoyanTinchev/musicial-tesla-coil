#ifndef MUSIC_H_
#define MUSIC_H_

#include <avr/io.h>
#include <avr/sfr_defs.h>

#define AUD_PIN 4

struct addr_t{
	uint16_t last_addr;
	int size;
} music_addr;

void play(int);
void stop(void);

#endif