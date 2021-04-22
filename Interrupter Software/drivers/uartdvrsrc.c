#include "includes/uartdvrinc.h"

void UART_enable(void){
	UBRR0L = UBRRL_VALUE;
	UBRR0H = UBRRH_VALUE;
	UCSR0B = _BV(TXEN0) | _BV(RXEN0);
}

void UART_disable(void){
	UCSR0B = 0;
}

void UART_double_speed(int flag){
	if (flag)
		UCSR0A |= _BV(U2X0);
	else
		UCSR0A &= ~_BV(U2X0);
}

void UART_write_byte(uint8_t data){
	loop_until_bit_is_set(UCSR0A, UDRE0);
	UDR0 = data;
}

uint8_t UART_read_byte(void){
	loop_until_bit_is_set(UCSR0A, RXC0);
	return (uint8_t) UDR0;
}
