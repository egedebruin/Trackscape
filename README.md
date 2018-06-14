# TrackScape
BEP

[![BCH compliance](https://bettercodehub.com/edge/badge/egedebruin/Trackscape?branch=master)](https://bettercodehub.com/)

[![Build status](https://ci.appveyor.com/api/projects/status/wyfdfhpg79289ceg?svg=true)](https://ci.appveyor.com/project/Jasperkroes/trackscape)

**Description**
This product aims to assist hosts of Escape Rooms in the process of observing players during an Escape Room game. It intents to give a clear overview of the current situation of the game. 

**Instructions for OpenCV**

* Download OpenCV from https://opencv.org/releases.html
    * For Windows: Choose 3.4.1 Win Pack
* Run downloaded .exe and extract files in self-selected map
* Copy opencv\build\java\x64\opencv_java341.dll to Trackscape/libs
* Copy opencv\build\bin\opencv_ffmpeg341_64.dll to Trackscape/libs

[**OpenCV javafx examples**](https://github.com/opencv-java/opencv-java-tutorials)


**Instructions for working with the API**

When a configuration is started, a server will also be available for API calls. The API calls will be processed only when 
activity is found on the cameras. 

The port of the API calls can be determined in the config file, when it isn't given the default port is 8080. There are two API calls:

CALL=
* section?completed=true
* chest?opened=true

The first call sets the next section to completed. The second call sets the next chest to opened.
The API call has the following structure:

\[IPADDRESS\]:\[PORT\]/\[CALL\]

For example, the following call opens the next chest of a room which listens to port 8080 on a server on the same computer:

http://localhost:8080/chest?opened=true

A section can also be a chest. When all sections of the to be opened chest are already done and the next section is completed,
the chest will be opened.