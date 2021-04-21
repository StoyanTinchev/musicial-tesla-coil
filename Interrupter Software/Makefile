FLAGS = -Os -Wall -mmcu=atmega328p -c

DRIVERS_SRC = drivers
DRIVERS_INC = drivers/includes

compile: main.o drivers
	avr-gcc main.o ${DRIVERS_SRC}/eepromdvr.o ${DRIVERS_SRC}/uartdvr.o -o main.elf
	avr-objcopy -O ihex main.elf main.hex
	rm main.elf main.o ${DRIVERS_SRC}/uartdvr.o ${DRIVERS_SRC}/eepromdvr.o

main.o: main.c 
	avr-gcc ${FLAGS} main.c -o main.o 

drivers: uartdvr eepromdvr

uartdvr: ${DRIVERS_SRC}/uartdvrsrc.c ${DRIVERS_INC}/uartdvrinc.h
	avr-gcc ${FLAGS} ${DRIVERS_SRC}/uartdvrsrc.c -o ${DRIVERS_SRC}/uartdvr.o

eepromdvr: ${DRIVERS_SRC}/eepromdvrsrc.c ${DRIVERS_INC}/eepromdvrinc.h
	avr-gcc ${FLAGS} ${DRIVERS_SRC}/eepromdvrsrc.c -o ${DRIVERS_SRC}/eepromdvr.o

upload: compile