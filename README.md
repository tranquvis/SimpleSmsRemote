# Simple sms remote 
Android app for remotely controlling a phone through sms messages.
Install the app on a device, which should be controlled, and send control commands from any messenger to it.

**compatible android versions:** 4.0.3 (Ice Cream Sandwich) and higher

![Logo](https://raw.githubusercontent.com/tranquvis/SimpleSmsRemote/master/.github/logo.png)

#### Will be available in Play Store soon!
**status:** under development

[download alpha](https://raw.githubusercontent.com/tranquvis/SimpleSmsRemote/master/app/app-release-alpha-0.10.apk)

## Features
* specify which modules are accessible and which phones are granted
* start sms receiver after boot
* send multiple commands with one message
* reply to sender phone with result message
* show permanent notification with receiver status
* view log of sms receiver

### Modules
* Hotspot: enable, disable, check if enabled
* Wifi: enable, disable, check if enabled
* Bluetooth: enable, disable, check if enabled
* Mobile Data Connection: enable, disable, check if enabled
* Battery: fetch level, check if battery is charging
* Location: fetch coordinates
* Audio Volume: set volume of ringtone and music, fetch volume

## Security
Granted phones are required to be set for each module, so not everyone can control the device.
However, if someone fakes his phone number he is able to use all enabled modules. (Note that this might be a complicated and illegal procedure. Moreover also common antitheft apps like avast trust the sender's phone number.

***
**License: [MIT](LICENSE)**

