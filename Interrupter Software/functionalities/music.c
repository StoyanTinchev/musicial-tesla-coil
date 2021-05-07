#include "includes/music.h"

void play(int size){
	PORTB |= _BV(AUD_PIN);
}

void stop(void){
	PORTB &= ~_BV(AUD_PIN);
}