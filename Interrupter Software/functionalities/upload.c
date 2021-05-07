#include "includes/upload.h"

void upload(void){
	int addr = 0;

	while(1){
		uint8_t data = wait4command();

		if(isCommand(data)){
			if (data == 0xC1){
				music_addr.size = addr + 1;
				return;
			}
		}

		_writeByte(addr, data);
		addr ++;
	}
}