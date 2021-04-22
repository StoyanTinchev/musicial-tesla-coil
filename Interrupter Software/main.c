#include <avr/io.h>

#include "drivers/includes/eepromdvrinc.h"
#include "drivers/includes/uartdvrinc.h"
#include "functionalities/includes/command.h"
#include "functionalities/includes/music.h"
#include "functionalities/includes/upload.h"

int main(void){
	_initEeprom();
	UART_enable();
	music_addr.last_addr = 0;
	music_addr.size = 0;

	while(1){
		uint8_t com = wait4command();

		if(!isCommand(com))
			continue;

		switch(com){
			case 0xC0:
				upload();

			case 0xC2:
				play(music_addr.size);

			case 0xC3:
				stop();
		}
	}

	return 0; 
}