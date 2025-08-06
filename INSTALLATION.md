# WhatsApp Auto Responder - Installation Guide

## Prerequisites

### For Building the APK:
1. **Android Studio** (recommended) or Android SDK
2. **Java Development Kit (JDK)** 8 or higher
3. **Android SDK** with API level 24+ (Android 7.0)

### For Running the App:
1. **Android Device** running Android 7.0 or higher
2. **WhatsApp** installed on the device
3. **Internet connection** for scheduled messages

## Building the APK

### Method 1: Using Android Studio (Recommended)

1. **Open the project in Android Studio**:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project folder and select it

2. **Sync the project**:
   - Wait for Gradle sync to complete
   - Install any missing SDK components if prompted

3. **Build the APK**:
   - Go to Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for the build to complete
   - The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

### Method 2: Using Command Line

1. **Set up Android SDK**:
   ```bash
   export ANDROID_HOME=/path/to/your/android/sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

2. **Build using Gradle**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Find the APK**:
   The APK will be at `app/build/outputs/apk/debug/app-debug.apk`

### Method 3: Using the Build Script

1. **Make sure Android SDK is set up**:
   ```bash
   export ANDROID_HOME=/path/to/your/android/sdk
   ```

2. **Run the build script**:
   ```bash
   ./build.sh
   ```

## Installing the APK

### Step 1: Enable Unknown Sources
1. Go to **Settings** → **Security** (or **Privacy**)
2. Enable **"Unknown Sources"** or **"Install unknown apps"**
3. Allow installation from your file manager

### Step 2: Install the APK
1. Copy the APK file to your Android device
2. Open the APK file using your file manager
3. Tap **"Install"** when prompted
4. Wait for installation to complete

### Step 3: Grant Permissions
1. Open the **WhatsApp Auto Responder** app
2. Grant the following permissions when prompted:
   - **Accessibility Service** (required for auto-reply)
   - **Notification Access** (for detecting messages)
   - **Storage Access** (for saving settings)

## Setting Up the App

### Step 1: Enable Accessibility Service
1. Open the app
2. Tap **"Enable Accessibility Service"**
3. Go to **Settings** → **Accessibility**
4. Find **"WhatsApp Auto Responder"** and enable it
5. Return to the app

### Step 2: Configure Auto Reply
1. In the **"Auto Reply Settings"** section:
   - Toggle **"Enable Auto Reply"** to ON
   - Enter your custom auto-reply message
   - Choose who to reply to:
     - **All Messages**: Reply to everyone
     - **Contacts Only**: Reply only to saved contacts
     - **Specific Numbers**: Reply only to specific numbers
2. Tap **"Save Auto Reply Settings"**

### Step 3: Set Up Scheduled Messages
1. Tap the **"+"** button in the **"Scheduled Messages"** section
2. Fill in the details:
   - **Message**: The text to send
   - **Phone Number**: Recipient's number with country code (e.g., +1234567890)
   - **Date & Time**: When to send the message
   - **Repeat**: Choose frequency (No Repeat, Daily, Weekly, Monthly)
3. Tap **"Save Schedule"**

## Troubleshooting

### Build Issues

**"Gradle wrapper not found"**:
```bash
# Download the gradle wrapper jar
curl -o gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar
```

**"Android SDK not found"**:
- Install Android Studio and Android SDK
- Set ANDROID_HOME environment variable
- Install required SDK platforms (API 24+)

**"Java not found"**:
- Install JDK 8 or higher
- Set JAVA_HOME environment variable

### Installation Issues

**"App not installing"**:
- Enable "Unknown Sources" in device settings
- Check if the APK is corrupted (re-download)
- Ensure device has enough storage space

**"App crashes on startup"**:
- Clear app data and cache
- Reinstall the app
- Check device compatibility (Android 7.0+)

### Runtime Issues

**"Auto-reply not working"**:
- Ensure accessibility service is enabled
- Check that WhatsApp is installed and updated
- Verify auto-reply is enabled in app settings
- Restart the app and device

**"Scheduled messages not sending"**:
- Check notification permissions
- Disable battery optimization for the app
- Verify phone number format (include country code)
- Ensure scheduled time is in the future

## Security Notes

- The app requires accessibility permissions to automate WhatsApp
- All data is stored locally on your device
- No data is sent to external servers
- Use responsibly and ethically

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Ensure all permissions are granted
3. Restart the app and device
4. Check device compatibility requirements

## Legal Disclaimer

This app is for educational and personal use. Users are responsible for:
- Complying with WhatsApp's Terms of Service
- Respecting privacy and consent when auto-replying
- Using the app responsibly and ethically
- Ensuring they have permission to send automated messages

The developers are not responsible for any misuse of this application.