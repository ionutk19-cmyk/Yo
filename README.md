# WhatsApp Auto Responder

A comprehensive Android application that automatically responds to WhatsApp messages and sends scheduled messages at specified times.

## Features

### 🔄 Auto Reply
- **Automatic Responses**: Automatically reply to incoming WhatsApp messages
- **Customizable Messages**: Set your own auto-reply message
- **Reply Options**: Choose to reply to all messages, contacts only, or specific numbers
- **Smart Detection**: Uses accessibility services to detect new messages

### ⏰ Scheduled Messages
- **Time-based Sending**: Schedule messages to be sent at specific dates and times
- **Recurring Messages**: Set daily, weekly, or monthly recurring messages
- **Multiple Schedules**: Create and manage multiple scheduled messages
- **Contact Management**: Send to specific phone numbers with country codes

### 🛠️ Advanced Features
- **Background Processing**: Works even when the app is closed
- **Boot Persistence**: Automatically starts on device restart
- **Modern UI**: Material Design 3 interface
- **Database Storage**: Local storage for scheduled messages
- **Work Manager**: Reliable background task scheduling

## Setup Instructions

### Prerequisites
- Android device running Android 7.0 (API 24) or higher
- WhatsApp installed on the device
- Internet connection for scheduled messages

### Installation

1. **Build the APK**:
   ```bash
   ./gradlew assembleDebug
   ```
   The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

2. **Install the APK**:
   - Enable "Unknown Sources" in your Android settings
   - Install the APK file on your device

3. **Enable Accessibility Service**:
   - Open the app
   - Tap "Enable Accessibility Service"
   - Go to Settings > Accessibility
   - Find "WhatsApp Auto Responder" and enable it

4. **Grant Permissions**:
   - Allow notification access when prompted
   - Grant any additional permissions requested

### Configuration

#### Auto Reply Setup
1. Open the app
2. In the "Auto Reply Settings" section:
   - Toggle "Enable Auto Reply" to ON
   - Enter your custom auto-reply message
   - Choose who to reply to (All, Contacts, or Specific)
3. Tap "Save Auto Reply Settings"

#### Scheduled Messages Setup
1. Tap the "+" button in the "Scheduled Messages" section
2. Fill in the details:
   - **Message**: The text to send
   - **Phone Number**: Recipient's number with country code (e.g., +1234567890)
   - **Date & Time**: When to send the message
   - **Repeat**: Choose frequency (No Repeat, Daily, Weekly, Monthly)
3. Tap "Save Schedule"

## How It Works

### Auto Reply Mechanism
The app uses Android's Accessibility Service to:
- Monitor WhatsApp notifications and window changes
- Detect when new messages arrive
- Automatically type and send your configured reply
- Work in the background without user intervention

### Scheduled Messages
The app uses WorkManager to:
- Check for scheduled messages every 15 minutes
- Open WhatsApp with the specified contact
- Send the message at the exact scheduled time
- Handle recurring messages automatically

## Technical Details

### Architecture
- **MVVM Pattern**: ViewModel and LiveData for reactive UI
- **Room Database**: Local storage for scheduled messages
- **WorkManager**: Background task scheduling
- **Accessibility Service**: WhatsApp automation
- **Material Design 3**: Modern UI components

### Key Components
- `MainActivity`: Main UI and user interactions
- `WhatsAppAccessibilityService`: Handles WhatsApp automation
- `ScheduledMessageWorker`: Manages scheduled message sending
- `MainViewModel`: Business logic and data management
- `ScheduledMessageRepository`: Database operations

### Permissions Required
- `BIND_ACCESSIBILITY_SERVICE`: For WhatsApp automation
- `SYSTEM_ALERT_WINDOW`: For overlay detection
- `POST_NOTIFICATIONS`: For notification monitoring
- `RECEIVE_BOOT_COMPLETED`: For auto-start on boot
- `INTERNET`: For scheduled message functionality

## Troubleshooting

### Common Issues

**Auto-reply not working:**
- Ensure accessibility service is enabled
- Check that WhatsApp is installed and updated
- Verify auto-reply is enabled in app settings
- Restart the app and device if needed

**Scheduled messages not sending:**
- Check that the app has notification permissions
- Ensure the device is not in battery optimization mode
- Verify the phone number format (include country code)
- Check that the scheduled time is in the future

**App crashes or doesn't start:**
- Clear app data and cache
- Reinstall the app
- Ensure all permissions are granted

### Performance Tips
- Keep the app in the background for best performance
- Avoid battery optimization for this app
- Use specific phone numbers rather than "reply to all" for better reliability

## Security & Privacy

- **Local Storage**: All data is stored locally on your device
- **No Cloud Sync**: No data is sent to external servers
- **Permission Based**: Only requests necessary permissions
- **Open Source**: Code is available for review

## Development

### Building from Source
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device

### Dependencies
- AndroidX Core KTX
- Material Design 3
- Room Database
- WorkManager
- Lifecycle Components
- Coroutines

## License

This project is open source and available under the MIT License.

## Support

For issues, feature requests, or questions:
- Check the troubleshooting section above
- Review the code for technical details
- Ensure all permissions are properly granted

## Disclaimer

This app is for educational and personal use. Users are responsible for:
- Complying with WhatsApp's Terms of Service
- Respecting privacy and consent when auto-replying
- Using the app responsibly and ethically
- Ensuring they have permission to send automated messages

The developers are not responsible for any misuse of this application.