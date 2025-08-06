# WhatsApp Auto Reply & Scheduler

An Android app that automatically replies to WhatsApp messages based on keywords and allows scheduling messages to be sent at specific times.

## Features

- **Auto Reply**: Set up keyword-based automatic replies
- **Message Scheduling**: Schedule messages to be sent at specific times
- **Easy Management**: Simple UI to manage rules and scheduled messages
- **Accessibility Service**: Uses Android's accessibility service to interact with WhatsApp

## Building the APK

### Prerequisites
- Android Studio (recommended) or command line tools
- Android SDK
- Java Development Kit (JDK) 8 or higher

### Build Steps

1. **Using Android Studio:**
   - Open the project in Android Studio
   - Wait for Gradle sync to complete
   - Go to Build → Build Bundle(s) / APK(s) → Build APK(s)
   - The APK will be generated in `app/build/outputs/apk/debug/`

2. **Using Command Line:**
   ```bash
   cd /workspace/WhatsAppAutoReply
   ./gradlew assembleDebug
   ```
   The APK will be in `app/build/outputs/apk/debug/app-debug.apk`

## Installation

1. Enable "Install from Unknown Sources" in your Android device settings
2. Transfer the APK to your device
3. Install the APK by tapping on it

## Setup

1. **Enable Accessibility Service:**
   - When you first open the app, it will prompt you to enable accessibility
   - Go to Settings → Accessibility → WhatsApp Auto Reply
   - Enable the service

2. **Grant Permissions:**
   - The app will request necessary permissions
   - Make sure to grant all requested permissions

## Usage

### Auto Reply
1. Tap the floating action button (+) on the Auto Reply tab
2. Enter a keyword to trigger the reply
3. Enter the message to send as a reply
4. Save the rule
5. Toggle the switch to enable/disable individual rules

### Scheduled Messages
1. Switch to the Scheduled Messages tab
2. Tap the floating action button (+)
3. Enter the contact name
4. Enter the message to send
5. Select the date and time
6. Save the scheduled message

## Important Notes

- The app requires WhatsApp to be installed on your device
- Keep the app running in the background for auto-replies and scheduled messages to work
- The accessibility service must remain enabled
- Battery optimization should be disabled for this app to ensure reliable operation

## Security & Privacy

- The app only processes messages locally on your device
- No data is sent to external servers
- All auto-reply rules and scheduled messages are stored locally

## Troubleshooting

- **Auto-reply not working**: Make sure accessibility service is enabled and the app has all permissions
- **Scheduled messages not sending**: Ensure the app is not being killed by battery optimization
- **Can't find accessibility service**: Reinstall the app and try again

## Limitations

- The app works only with the official WhatsApp application
- Requires Android 5.0 (API 21) or higher
- May not work if WhatsApp updates change their UI structure significantly