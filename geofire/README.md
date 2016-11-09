# Description

A sample for [GeoFire](https://github.com/firebase/geofire).

GeoFire is set of open-source libraries for JavaScript, Objective-C, and Java that allow you to store and query a set of keys based on their geographic location.  
At its heart, GeoFire simply stores locations with string keys.  
Its main benefit, however, is the possibility of retrieving only those keys within a given geographic area - all in realtime.

GeoFire uses the Firebase database for data storage, allowing query results to be updated in realtime as they change.  
GeoFire selectively loads only the data near certain locations, keeping your applications light and responsive, even with extremely large datasets.

# Required

If you just download this sample application, the build fail.

You need to create a Firebase project, and download a ```google-services.json``` flie.  
After downloading the ```google-services.json```, copy this into your project's module folder, typically ```app/```.  

And, you must add SHA-1 fingerprint of your application to Firebase.

[How to add Firebase to your Android Project](https://firebase.google.com/docs/android/setup#add_firebase_to_your_app)

# Getting Started

1. Add locations  
When you click the add button, the location is stored in the Firebase.

1. Remove added locations  
When you click the remove button, the added location is removed from the Firebase.
3. Search added locations  
When you click the search button, search the location that saved in the Firebase.