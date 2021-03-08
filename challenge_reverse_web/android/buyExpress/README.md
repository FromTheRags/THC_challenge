
# PIR: android app !

This repository is a AndroidStudio project of an android application to use more efficiently our **INSA shuttle application**. 

## Configuration

This project is not in production yet, meaning there is not a server online to manage the car, just a local node js server to start on your local network ([the node server](https://github.com/SuperTeam1nsa/PIR_website/tree/372a7337931f0c8842771b4be7517a1e7f6cf31c))
So you need to configure the IP of the server to connect:
### 1. With Android Studio:
*  Open the project in Android Studio
*  edit file `strings.xml` in `app/src/main/res/values` with the good ip/URL/port
    

![Android Studio view](https://github.com/SuperTeam1nsa/PIR_androidWebApp/help/main_config_file.jpg)

### 2. With ApkTool:  
 Alternatively, if do not want to use android studio, you can use apktools and sign the application (if the app is already installed, you will have to uninstall it, as the signature will be different):  
```cd app/release  
    java -jar apktool_2.3.4.jar d app.apk -o appcustom
    cd appcustom/res/values
    edit file strings.xml (field IP, URL, PORT)
    cd ../../..
    java -jar apktool_2.3.4.jar b appcustom -o appcustom.apk
```
  Then to sign the apk :  
*   Generate a certificat (only once, fill the fields with whatever you want) :  
  `  keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000`  
*   Sign with this certificat:  
  `jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore appcustom.apk alias_name`  
  
**NB:** Last version of [apktool](https://ibotpeaches.github.io/Apktool/) have issues with the format of ressource, so use 2.3.4 to be compatible
### 3. With default values 
 Or if you do not want to deal with apk, just configure your local network to put the server on `http://192.168.43.193:8080`.

## Installation

### 1.    Via Android Studio :
1.  You'll first need to unlock the hidden Developer options menu on your device. To begin, head to the main Settings menu, then select About phone, About tablet, or About device. From here, tap the "Build number" entry 7 times in rapid succession, and you'll get a toast message informing you that the Developer options menu has been unlocked.  From here, just back out to the main Settings menu again, and you'll find the new "Developer options" entry near the bottom, so go ahead and select it.  
2.   In this menu search for USB debugging and turn it on.
3. Connect the phone to the PC via USB 
4. ` Alt+Maj+F11` or on the top of android studio select your phone
5. ` Maj+F10` or click on the green arrow(run app)
6. The app is now installed and running on your phone

**NB:** if you want to share to others a modified version of this app you can [generate and sign an apk from Android studio](https://developer.android.com/studio/publish/app-signing#sign-apk) too

### 2. Via apk

Connect your phone to the PC, transfer the apk file and launch it from the phone to begin the installation. You will be asked a permission, either wait for the message that will lead you to the parameter or manually go :   
https://www.theandroidsoul.com/how-to-allow-apps-installation-from-unknown-sources-on-android-9-pie/

## Run

Just click on the application icon, like all your other application  
(look at the nice icon by the way :stuck_out_tongue_winking_eye:)


