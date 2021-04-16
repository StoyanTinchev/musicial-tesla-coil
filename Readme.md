


Reworked musical tesla coil
========
<br><br>




Description:
--------
Reworked musical tesla coil in smaller scaling. Using an application with which we choose a song and send it to the coil interrupter.<br>
We will have 2 applications for mobile android phones – one complete mp3 player, that play/pause songs and all that trivial operations. Second – app that list all “.wav” files and than with holding chosen song – it will arrive new dialog window that asks the user if he want to send it via Bluetooth to the interrupter or cancel that operation.<br>
Then the interrupter sends out modified pulses which interrupt the coil supply so that the output to the coil has the same frequency as the audio signal.<br>
The software for the interrupter will wait for upload command, after which the program reads the byte sequence and stores it in an external flash memory. If a play command is sent the program reads the content of that memory and converts it into respective PWM(Pulse Width Modulation) signal.
<br><br><br>


Technologies:
--------
1. Java<br>
2. HTML<br>
3. C / C++<br>
4. Embedded<br>
<br><br>



Management:
--------
Branching Strategy – GitHub Flow<br>
Strategy of development - Kanban
<br><br><br>



Authors: Stoyan Tinchev and Alexandar Alexandrov
--------


