# Description

A sample for Firebase Invites.  

Firebase Invites is a cross-platform solution for sending personalized email and SMS invitations, on-boarding users,
and measuring the impact of invitations.  

Firebase Invites builds on Firebase Dynamic Links.  
While Dynamic Links ensure that recipients of links have the best possible experience for their platform and the apps they have installed,
Firebase Invites ensures the best possible experience for sending links.

You can check the app launch at the time of link tap in this sample app.  
You can not confirm the installation.

# Required

You need to create a Firebase project, and download a ```google-services.json``` flie.  
After downloading the ```google-services.json```, copy this into your project's module folder, typically ```app/```.  

And, you must add SHA-1 fingerprint of your application to Firebase.

[How to add Firebase to your Android Project](https://firebase.google.com/docs/android/setup#add_firebase_to_your_app)


# Getting Started

1. Launch this sample app  
After launching this sample app, the screen will be displayed.  
When you click the button on the screen, message sent activity will be launched.

1. Select recipients and send message  
Select recipients, please tap the send icon.

1. Receive invitation  
You will receive invitation.  

Please tap the link on invitation message. This sample app is launched. Not displayed install page, because app is not published in Google Play Store.  
Deep link and invitation ID will be displayed on the app screen.