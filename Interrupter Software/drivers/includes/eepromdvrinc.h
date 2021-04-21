#ifndef EEPROMDVRINC_H_
#define EEPROMDVRINC_H_

#include <avr/eeprom.h>
#include <avr/sfr_defs.h>

#define SIZE 1024

void _initEeprom(void);
uint8_t _readByte(uint16_t);
void _writeByte(uint16_t, uint8_t);

#endif