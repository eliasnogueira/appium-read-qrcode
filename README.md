# Appium Read QRCode

This project shows how to read the QRCode content from an Android App using Appium and ZXing.

## Technologies in use
* [Java](https://www.oracle.com/java/technologies/javase-downloads.html) as the programming language
* [Appium](http://appium.io/) as the mobile test automation tool
* [Zxing](https://github.com/zxing/zxing) as the library to decode the QRCode content
* [AssertJ](https://joel-costigliola.github.io/assertj/) as the assertion library
* [JUnit 5](https://junit.org/junit5/) as the test tool to support the test automation script

## How to run this project

### Preconditions
- Java JDK >=11
- Android Emulator with `minSdkVersion` used as 16 (Android 4.1 Jelly Bean)

### Steps

### Running using the command line
1. Go do the project directory and run `mvn verify -Dmaven.test.skip=true`
2. Run `mvn test` to run the test

### Running in your IDE
1. Open this project in your preferred IDE
2. Open the `ReadQRCodeTest` class placed in `src/test/java`
3. Run the test

### Expected result
You can expect a successful execution.
The test will read the QRCode content and assert by its expectation.

## What does the test do
The code does the following:
- Open an Android Emulator (if it's not opened)
- Install the apk placed on `app` folder
- Open the app main screen
- Takes a screenshot of the screen that has the QrCode
- Send the QRCode screenshot, as Base64, to be decoded by ZXing
- Return the QRCode content
- Assert the QRCode content

**Attention**
You need to have all the necessary configurations to run the test. This project has no intention to describe how you can do it.