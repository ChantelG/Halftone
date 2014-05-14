Brief Overview
--------------
Ye Olde Times is an application that allows a user to transform an original image to a black-and-white halftoned image. Such an image is similar to that which can be seen in an old newspaper where the image pixels are replaced with shapes like circles, diamonds or squares. The application also allows users to add a caption to their image, adding additional context to their halftoned image. Ye Olde Times also allows a user to share their halftoned image via social media such as Facebook, Twitter (or even via email if they so desire). 


Who is Responsible?
--------------------
Ye Olde Times application is created by Chantel Garcia and Carmen Pui.


Implementation of the Application
----------------------------------
In this release, Ye Olde Times allows its users to choose an image from the Gallery or capture an image via the Camera on their device. The halftoning of the image is implemented such that it can be performed using circle, diamond or square shapes. Users are also able to add text at the bottom of the image (in the form of a caption). Finally, users are also able to share their image (with or without a caption) with various social media services.

Automated test have also been written for the application and are included in the source code (navigate to "Workspace->YeOldeTimesTests" to view the automated tests. To run them, simply open the workspace called "Workspace" as your workspace in Eclipse. Then, right click on the "YeOldeTimesTests" folder and select "RunAs->AndroidJUnitTest".


Getting Started
---------------
1) Start up the "Ye Olde Times" application on the device by clicking on its app icon (an icon that has a wood background with a T on top). (Please Note: Before you attempt to do this, please make sure that it is already installed properly into the device)
2) On the home screen of the "Ye Olde Times" application, select either the Gallery or Camera button (to upload your image from the gallery, select Gallery otherwise to obtain an image using the camera on the device, select Camera).
3) If the Gallery button is selected, select "Upload Image" and choose an image from the Image Gallery. If the Camera is selected, select "Capture Image" and then the camera will be started up allowing you to capture a picture. Then click on the circle in the centre of the bottom of the screen and click done when you are finished (note that this will appear as a tick on the Nexus 7 device itself, but as "done" in the Android Nexus 7 emulator).
4) Choose "Next" to go to the next screen.
5) On this screen, choose a desired shape (circle, diamond or square) to halftone your image with.
6) If you wish to add caption to the image, add a caption in the text area and select "Update Caption". You can also remove the caption by selecting "Remove Caption". Click "Next" when you are finished.
7) Select "OK" to save the image when the pop-up appears.
8) Click the "Click To Share" button and select a social networking service (or email) to share the image with. 
9) Click the "Finish" button to return to the home screen.


Known Bugs and Limitations
--------------------------
- Captions can only be added at the bottom of the image.
- The image must be saved before sharing via social media (or email).
- The screen is locked in portrait mode.