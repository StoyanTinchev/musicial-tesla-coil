


Reworked musical tesla coil
========
<br><br>





Authors: Stoyan Tinchev and Alexandar Alexandrov
--------
<br><br><br>




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



First Milestone:
--------
1. Creating an android app with android studio and java that lists all “.wav” files from phone<br>
	a. Be able to work with all android mobile phones<br>
	b. Be able to upload it to the phone<br>
2. Creating a project schematic
3. The interrupter software<br>
	a. Be able to read song via Bluetooth<br>
	b. Be able to split and store it<br>
	c. Output value dependent PWM signal
<br><br><br>




Second Milestone:
--------
1. Building the hardware<br>
	a. Creating the SSTC(Solid State Tesla Coil) driver<br>
	b. Creating the SSTC interrupter<br>
2. Upgrade phone application so you can send one of the pre-listed “.wav” files via bluetooth<br>
3. Creating an complete mp3 player for mobile phone with android studio.<br>
	a. Be able to play/pause song<br>
	b. Be able to switch songs<br>
	c. Be able to create albums
<br><br><br>



Third Milestone:
--------
1. Upgrade the design<br>
2. Code reviews and optimizations<br>
3. Unit tests
<br><br><br>




Optional:
--------
1. Adding AUX input to the interrupter<br>
	a. Be able directly from the computer to play song to the tesla coil
<br><br><br>




Александър Александров 11А, Стоян Тинчев 11А

