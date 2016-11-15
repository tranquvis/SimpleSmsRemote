# Simple sms remote

Android app for remotely controlling a phone through sms messages.
Install the app on a device, which should be controlled, and send control commands from any messenger to it.

**compatible android versions:** 4.1 (Jelly Bean) and higher

![Logo](https://raw.githubusercontent.com/tranquvis/SimpleSmsRemote/master/.github/logo.png)

**Get the installer directly from [Github](https://raw.githubusercontent.com/tranquvis/SimpleSmsRemote/master/app/app-release-beta-1.0.0.apk) or on [Google Play](https://play.google.com/store/apps/details?id=tranquvis.simplesmsremote&hl=de).**

<a href='https://play.google.com/store/apps/details?id=tranquvis.simplesmsremote&hl=de&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' width="200px" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

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
* Battery: get level, check if battery is charging
* Location: get current device coordinates
* Audio Volume: set and get volume of all audio types (ringtone, music, ...)
* Display: set and get brightness, set and get display off timeout, turn display off
* Camera: take photo with default settings or custom options (flash, autofocus and more)

## Security
Granted phones are required to be set for each module, so not everyone can control the device.
However, if someone fakes his phone number he is able to use all enabled modules. (Note that this might be a complicated and illegal procedure. Moreover also common antitheft apps like avast trust the sender's phone number.

***
**License: [MIT](LICENSE)**

