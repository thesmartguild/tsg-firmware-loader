# tsg-firmware-loader
GUI based software for loading firmware onto bricked (and non-bricked) routers.  Primary use is for development and testing of OpenWrt.

#What is tsg-firmware-loader?
The tsg-firmware-loader is a java program the makes it easy to install a new firmware image on a router.
The goal is to make the process as turn-key as possible.

#How to use the tsg-firmware-loader
To use the firmware loader first you will need to have the firmware loader jar and then make sure that the router is connected to
the computer with both a ethernet cable and a serial adapter.

After that double click the jar and drag and drop your new firmware image on to the GUI that pops up.

Once the MD5 for the image is calculated you can click install.

if you’re running windows:
 	You will get 2 pop-ups asking for admin privileges, these are needed in order to temporarily
  change the network settings. if you do not allow this the firmware installer most likely will not work.
  
Now that you have clicked install simply power cycle your router and sit back and relax.

#Current limitations
At this time the firmware loader will only work on routers running the U-Boot bootloader.
Also the program will only change the network settings on devices running MS Windows.

#Workarounds
If running on another OS please manually change your network setting to have a static ip of 192.168.1.3

#How to build from source
In order to build the project from source you will need to have gradle installed. http://gradle.org/

Once you have gradle installed all you need to do is run the gradle build task.

>gradle build

