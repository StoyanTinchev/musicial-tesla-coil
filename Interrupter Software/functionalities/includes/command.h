#ifndef COMMAND_H_
#define COMMAND_H_

#include "../../drivers/includes/uartdvrinc.h"

uint8_t wait4command(void);
int isCommand(uint8_t);

#endif