# Go4Lunch
This app helps to choose a restaurant for lunch with workmates.
It's Android App.
Application developed for project 6 (OppenClassrooms)
## Installation
Clone this repository and import into Android Studio
## Firebase Configuration
You must create a project on firebase and add google-service.json in your project android studio. And also you have to add the authentication by google, facebook and twitter (adding your key in firebase)
## Keystores:
In android studio add this line in your local.propeties:
```
>apiKey="YOUR_GOOGLE_KEY"
>twitterKey="YOUR_TWITTER_API_SECRET_KEY"
```
And in your file "strings.xml" edit your keys:
```
    <!-- FACEBOOK PURPOSE -->
    <string name="facebook_application_id" translatable="false">"YOUR_FACEBOOK_ID_KEY</string>
    <string name="facebook_login_protocol_scheme" translatable="false" tools:ignore="UnusedResources">fb"YOUR8FACEBOOK_ID_KEY</string>

    <!-- Twitter PURPOSE -->
    <string name="twitter_consumer_key" translatable="false">"YOUR_TWITTER_API_KEY</string>
```
# Authors
Julien Darcos
