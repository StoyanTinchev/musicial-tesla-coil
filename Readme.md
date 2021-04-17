

<h1 align="center">Reworked musical tesla coil</h1> <br><br>


<h2>Description:</h2>
Reworked musical tesla coil in smaller scaling. Using an application with which we choose a song and send it to the coil interrupter.<br>
We will have 2 applications for mobile android phones – one complete mp3 player, that play/pause songs and all that trivial operations. Second – app that list all “.wav” files and than with holding chosen song – it will arrive new dialog window that asks the user if he want to send it via Bluetooth to the interrupter or cancel that operation.<br>
Then the interrupter sends out modified pulses which interrupt the coil supply so that the output to the coil has the same frequency as the audio signal.<br>
The software for the interrupter will wait for upload command, after which the program reads the byte sequence and stores it in an external flash memory. If a play command is sent the program reads the content of that memory and converts it into respective PWM(Pulse Width Modulation) signal.
<br><br>


## How to install:
With your android phone follow this <a href="https://drive.google.com/file/d/1hvmjaKKLcVOmwQG_RgDjd7zN8inDXilg/view">link</a>. Start the installation by choosing with what to open current file. <br>
<img src="installation_pictures/choose_package_manager.jpg" width=30%> 

Play protect, or your google api service may recognize it as virus but don't worry - just install! <br>
<img src="installation_pictures/click_install.jpg" width=30%>

After the installation is completed, you will see new icon with name "Music app". That's it! \
<img src="installation_pictures/app_icon.jpg" width=30%>
<br><br>


## Technologies:
1. Java<br>
2. HTML<br>
3. C / C++<br>
4. Embedded
<br>
   

## Management:
Branching Strategy – GitHub Flow<br>
Strategy of development - Kanban
<br><br>



## Authors: Stoyan Tinchev and Alexandar Alexandrov


