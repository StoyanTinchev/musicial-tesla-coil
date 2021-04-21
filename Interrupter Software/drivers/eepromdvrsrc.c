#include "includes/eepromdvrinc.h"

void _initEeprom(void){
	EECR = 0;
}

uint8_t _readByte(uint16_t addr){
	if (addr < 0 && addr >= SIZE)
		return -1;

	loop_until_bit_is_clear(EECR, EEPE);

	EEARL = addr & 0xFF;
	EEARH = addr >> 8;

	EECR |= _BV(EERE);

	return (uint8_t) EEDR;
}

int _writeByte(uint16_t addr, uint8_t data){
	if (addr < 0 && addr >= SIZE)
		return -1;

	loop_until_bit_is_clear(EECR, EEPE);

	EEARL = addr & 0xFF;
	EEARH = addr >> 8;

	EEDR = data;

	EECR |= _BV(EEMPE);
	EECR |= _BV(EEPE);

	return 0;
}		