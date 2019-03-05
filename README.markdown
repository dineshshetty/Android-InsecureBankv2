InsecureBankv2 Readme
==========

[![Black Hat Arsenal](https://www.toolswatch.org/badges/arsenal/2015.svg)](https://www.blackhat.com/eu-15/arsenal.html/)

[![Black Hat Arsenal](https://www.toolswatch.org/badges/arsenal/2016.svg)](https://www.blackhat.com/us-16/arsenal.html/)

This is a major update to one of my previous projects - "InsecureBank". This vulnerable Android application is named "InsecureBankv2" and is made for security enthusiasts and developers to learn the Android insecurities by testing this vulnerable application. Its back-end server component is written in python. It is compatible with Python2. The client component i.e. the Android InsecureBank.apk can be downloaded along with the source. The list of vulnerabilities that are currently included in this release are:

* Flawed Broadcast Receivers
* Intent Sniffing and Injection
* Weak Authorization mechanism
* Local Encryption issues
* Vulnerable Activity Components
* Root Detection and Bypass
* Emulator Detection and Bypass
* Insecure Content Provider access
* Insecure Webview implementation
* Weak Cryptography implementation
* Application Patching
* Sensitive Information in Memory
* Insecure Logging mechanism
* Android Pasteboard vulnerability
* Application Debuggable
* Android keyboard cache issues
* Android Backup vulnerability
* Runtime Manipulation
* Insecure SDCard storage
* Insecure HTTP connections
* Parameter Manipulation
* Hardcoded secrets
* Username Enumeration issue
* Developer Backdoors
* Weak change password implementation

Below are some of the other vulnerabilities that I am working on currently - and will be added as soon as I make sure that it does not break any of the other existing features:
* Weak Pseudo Random Implementation
* Path Traversal
* Local SQL Injection
* Intent based Denial-Of-Service - SMS
* LockScreen Bypass
* Location Spoofing
* Dead Code


If you are too impatient to use the application or read the usage guide then follow these steps:

1) Download and install latest apk file

2) Make sure that the AndroLab server is running

3) Make sure Is machine-machine access allowed on your network. Firewall disabled. Open netcat on your machine and then adb into your emulator. Try to connect to the address from adb and see if you can reach the machine. If you can not - fix the network issue before trying. I can not help you fix your network issues sadly so please there is no point creating git issues for it. 

4) Use the credentials dinesh/Dinesh@123$ or jack/Jack@123$ and start using the application

