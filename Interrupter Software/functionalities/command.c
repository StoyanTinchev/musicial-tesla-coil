#include "includes/command.h"

uint8_t wait4command(void){
	uint8_t data = UART_read_byte();
	return data;
}

int isCommand(uint8_t command){
	uint8_t commands[] = {0xC0, 0xC1, 0xC2, 0xC3};
	int idx;
	for (idx = 0; idx < 4; idx ++){
		if (commands[idx] == command){
			return 1;
		}
	}
	
	return 0;
}