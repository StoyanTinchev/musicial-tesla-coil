FLAGS = -Os -Wall -mmcu=atmega328p -c

DRIVERS_SRC = drivers
DRIVERS_INC = drivers/includes
FUNC_INC = functionalities/includes
FUNC_SRC = functionalities

compile: main.o drivers functionalities
	avr-gcc main.o ${DRIVERS_SRC}/eepromdvr.o ${DRIVERS_SRC}/uartdvr.o ${FUNC_SRC}/music.o ${FUNC_SRC}/upload.o ${FUNC_SRC}/command.o -o main.elf
	avr-objcopy -O ihex main.elf main.hex
	rm main.elf main.o ${DRIVERS_SRC}/uartdvr.o ${DRIVERS_SRC}/eepromdvr.o ${FUNC_SRC}/music.o ${FUNC_SRC}/upload.o ${FUNC_SRC}/command.o 

main.o: main.c 
	avr-gcc ${FLAGS} main.c -o main.o 

drivers: uartdvr eepromdvr

functionalities: command music upload

uartdvr: ${DRIVERS_SRC}/uartdvrsrc.c ${DRIVERS_INC}/uartdvrinc.h
	avr-gcc ${FLAGS} ${DRIVERS_SRC}/uartdvrsrc.c -o ${DRIVERS_SRC}/uartdvr.o

eepromdvr: ${DRIVERS_SRC}/eepromdvrsrc.c ${DRIVERS_INC}/eepromdvrinc.h
	avr-gcc ${FLAGS} ${DRIVERS_SRC}/eepromdvrsrc.c -o ${DRIVERS_SRC}/eepromdvr.o

command: ${FUNC_SRC}/command.c ${FUNC_INC}/command.h
	avr-gcc ${FLAGS} ${FUNC_SRC}/command.c -o ${FUNC_SRC}/command.o

music: ${FUNC_SRC}/music.c ${FUNC_INC}/music.h
	avr-gcc ${FLAGS} ${FUNC_SRC}/music.c -o ${FUNC_SRC}/music.o

upload: ${FUNC_SRC}/upload.c ${FUNC_INC}/upload.h
	avr-gcc ${FLAGS} ${FUNC_SRC}/upload.c -o ${FUNC_SRC}/upload.o

burn: compile
	avrdude -p m328p -e -v -c usbasp -P usb -U flash:w:main.hex:i