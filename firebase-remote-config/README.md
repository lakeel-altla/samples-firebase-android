# Description

This project is a sample app for Firebase Remote Config.

Firebase Remote Config is a cloud service that lets you change the behavior and appearance of your app without requiring users to download an app update.  
When using Remote Config, you create in-app default values that control the behavior and appearance of your app. Then, you can later use the Firebase console to override in-app default values for all app users or for segments of your userbase.  
Your app controls when updates are applied, and it can frequently check for updates and apply them with a negligible impact on performance.

# Required

You need to create a Firebase project, and download a ```google-services.json``` flie.
After downloading the ```google-services.json```, copy this into your project's module folder, typically ```app/```.  

And, you must add SHA-1 fingerprint of your application to Firebase.

[How to add Firebase to your Android Project](https://firebase.google.com/docs/android/setup#add_firebase_to_your_app)

# Getting Started

1. Launch this sample app  
After launching this sample app, the screen will be displayed.  
When you click the button on the screen, you will see a "Your price is $ 100".

1. Open your firebase project  
In order to confirm this sample app, you need to open your firebase project.

1. Open Remote Config menu  
Open the Remote Config of the left of the menu.

1. Add config parameters  
Please add below parameters.

| Parameter Key | Default Value | Notes: |
|:--|:--|:--|
| is_promotion_on | true | Set to false to disable promotional discounts |
| discount | 30 | Promotional discount, defined as the amount of dollars or another currency that is subtracted from the price. |

After adding the parameters, please click the button on the screen again.
you will see a "Your price is $ 70".
