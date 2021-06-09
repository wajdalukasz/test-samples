#!/bin/bash

#echo 'Extracting tests.zip...'
yes | unzip tests.zip

mv application.ipa application.zip

unzip application.zip

mv Payload/Runner.app Runner.app

# make sure build folder exists
mkdir -p build/ios/iphoneos

# move app to build folder
mv Runner.app build/ios/iphoneos/Runner.app


echo 'starting flutter doctor ...'
flutter-2.2.0 doctor


# Install flutter packages
flutter-2.2.0 pub get

flutter-2.2.0 drive -v --use-application-binary build/ios/iphoneos/Runner.app --target=test_driver/app.dart
