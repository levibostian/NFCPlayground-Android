# NFCPlayground-Android

Android app for trying different tasks using NFC technology.

# Capabilities

- [x] Scan a NFC tag from the home screen of the phone and have it launch the app by a specified Activity (using intent filters).

To get this working, download an app from the Play Store that can write NFC tags (NFC tools is popular). Write to it this URL: `https://app.levibostian.com/code.html?code=111`. You can replace `111` with anything. That part is dynamic.

Install the mobile app to your device. Go to your phone's settings to make sure NFC reading is on. Then, scan your NFC tag you just wrote to on the phone. You should see a dialog popup asking you if you want to launch this app.


- [x] Use `Android Application Records` to launch a specific app (based on namespace) or go to the Play Store for the app if it does not exist on the device.

This works for the app out of the box. All you need to do is use an app from the Play Store like NFC tools to write an `Android Application Records` to a NFC tag.

If you write `com.levibostian.nfcfun` to an NFC tag for the `Android Application Records`, and you have this app installed on your device, this app will be launched without showing a dialog asking the user what app to launch.

`Android Application Records` works with intent filters. You can, and probably should, have intent filters and `Android Application Records` to define how your app should behave.

Here is more info about `Android Application Records` taken from the official docs

```
If a tag contains an AAR, the tag dispatch system dispatches in the following manner:

1. Try to start an Activity using an intent filter as normal. If the Activity that matches the intent also matches the AAR, start the Activity.
2. If the Activity that filters for the intent does not match the AAR, if multiple Activities can handle the intent, or if no Activity handles the intent, start the application specified by the AAR.
3. If no application can start with the AAR, go to Google Play to download the application based on the AAR.
```


