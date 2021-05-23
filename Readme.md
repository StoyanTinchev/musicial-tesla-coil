

<h1 align="center">Reworked musical tesla coil</h1> <br>


<h2>Description:</h2>
Reworked musical tesla coil in smaller scaling. Using an application with which we choose a song and send it to the coil interrupter.<br>
We will have 2 applications for mobile android phones – one complete mp3 player, that play/pause songs and all that trivial operations. Second – app that list all “.wav” files and than with holding chosen song – it will arrive new dialog window that asks the user if he want to send it via Bluetooth to the interrupter or cancel that operation.<br>
Then the interrupter sends out modified pulses which interrupt the coil supply so that the output to the coil has the same frequency as the audio signal.<br>
The software for the interrupter will wait for upload command, after which the program reads the byte sequence and stores it in an external flash memory. If a play command is sent the program reads the content of that memory and converts it into respective PWM(Pulse Width Modulation) signal.
<br><br>


## How to install (only for android)
With your android phone follow this <a href="https://drive.google.com/drive/folders/1HosFVmRzxvioICeHo3vdVdEkMbiyzbBV?usp=sharing">link</a>. Start the installation by choosing with what to open current file. Then click install.<br>
<img src="installation_pictures/choose_package_manager.jpg" width=30%> 
<img src="installation_pictures/click_install.jpg" width=30%> \
Play protect, or your google api service may recognize it as virus but don't worry - just install! <br>
<img src="installation_pictures/play_protect.jpg" width=30%>

After the installation is completed, you will see new icon with name "Music app". That's it! \
<img src="installation_pictures/app_icon.jpg" width=30%>
<br><br>


## Technologies:
1. Java<br>
2. XML<br>
3. C / C++<br>
4. Embedded
<br>
   

## Management:
>Branching Strategy – [GitHub Flow](https://guides.github.com/introduction/flow/)

>>Our modification of this model will be that before pull request with master branch - a new branch developer must be created. In this branch, the pull requests are made to interconnected code (at the same time code reviews are made), which is previously divided into other branches. Then, before pulling a request from the developer to the master branch, you need to review the whole new feature.  
***"Talk is cheap. Show me the code."***

<br>

>Strategy of development - Kanban

<br><br>



## Authors: Stoyan Tinchev and Alexandar Alexandrov


