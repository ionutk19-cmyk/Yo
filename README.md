# WhatsApp Auto Responder

A powerful Android application that automatically responds to WhatsApp messages and sends scheduled messages at specified times.

## Features

### ✅ Auto Reply
- Create custom auto-reply rules based on keywords or contact names
- Configurable delay before sending replies
- Enable/disable auto-reply functionality
- Multiple response rules support

### ✅ Scheduled Messages
- Send messages to specific contacts at scheduled times
- Support for recurring messages (daily, weekly, monthly)
- Background processing using WorkManager
- Persistent scheduling across device reboots

### ✅ Accessibility Integration
- Uses Android Accessibility Service to monitor WhatsApp messages
- Automatically detects incoming messages and sender information
- Sends replies directly through WhatsApp interface

### ✅ Modern Android Architecture
- Built with Kotlin
- Room database for data persistence
- MVVM architecture with ViewModels
- Material Design UI components
- Background processing with WorkManager

## Project Structure

```
app/src/main/java/com/whatsappautoresponder/
├── MainActivity.kt                           # Main app activity with tabs
├── service/
│   └── WhatsAppAccessibilityService.kt      # Core accessibility service
├── database/
│   ├── AutoReplyRule.kt                     # Auto-reply rule entity
│   ├── ScheduledMessage.kt                  # Scheduled message entity
│   ├── AutoReplyRuleDao.kt                  # DAO for auto-reply rules
│   ├── ScheduledMessageDao.kt               # DAO for scheduled messages
│   └── AppDatabase.kt                       # Room database
├── ui/
│   ├── AutoReplyRulesFragment.kt            # Fragment for managing rules
│   ├── ScheduledMessagesFragment.kt         # Fragment for managing scheduled messages
│   ├── adapter/                             # RecyclerView adapters
│   └── viewmodel/                           # ViewModels for UI
├── receiver/
│   ├── BootReceiver.kt                      # Handles device boot events
│   └── ScheduledMessageReceiver.kt          # Handles scheduled message alarms
├── worker/
│   └── ScheduledMessageWorker.kt            # Background worker for scheduling
└── utils/
    ├── SharedPreferencesManager.kt          # App settings management
    └── AccessibilityUtils.kt                # Accessibility helper functions
```

## Required Permissions

The app requires several permissions to function properly:

- **Accessibility Service**: To read WhatsApp messages and send replies
- **Boot Completed**: To restart scheduling after device reboot
- **Wake Lock**: To ensure scheduled messages are sent on time
- **Foreground Service**: For background processing
- **Exact Alarms**: For precise message scheduling
- **System Alert Window**: For overlay functionality

## Setup Instructions

### Prerequisites
- Android Studio 4.1 or higher
- Android SDK API level 21 or higher
- Java 8 or higher

### Building the APK

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd WhatsApp-Auto-Responder
   ```

2. **Open in Android Studio:**
   - Import the project into Android Studio
   - Sync Gradle files
   - Resolve any dependency issues

3. **Build the APK:**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Or use Android Studio: Build → Build Bundle(s)/APK(s) → Build APK(s)

4. **Install on device:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Manual Build (if Gradle wrapper is not available)

If you don't have Gradle installed, you can manually create the project structure and use Android Studio to build:

1. Import the project into Android Studio
2. Let Android Studio handle Gradle sync
3. Build → Make Project
4. Build → Build Bundle(s)/APK(s) → Build APK(s)

## Installation and Setup

### 1. Install the APK
- Enable "Unknown sources" in your Android settings
- Install the generated APK file

### 2. Enable Accessibility Service
- Open the app
- Tap on the accessibility service status message
- Navigate to Settings → Accessibility → WhatsApp Auto Responder
- Enable the service

### 3. Configure Auto Reply Rules
- Open the "Auto Reply Rules" tab
- Tap the "+" button to add new rules
- Set keywords or contact names and corresponding responses

### 4. Schedule Messages
- Open the "Scheduled Messages" tab
- Tap the "+" button to add scheduled messages
- Set contact name, message, and time

## Usage

### Creating Auto Reply Rules

1. **Keyword-based rules**: Messages containing specific keywords trigger automatic replies
2. **Contact-based rules**: Messages from specific contacts trigger automatic replies
3. **Combined rules**: Both keyword and contact conditions can be used together

### Scheduling Messages

1. **One-time messages**: Send a message at a specific date and time
2. **Recurring messages**: Set daily, weekly, or monthly recurring messages
3. **Contact targeting**: Specify exact contact names for message delivery

### Settings

- **Auto Reply Delay**: Configure delay before sending automatic replies
- **Only When Away**: Option to only reply when user is not actively using WhatsApp
- **Service Status**: Monitor accessibility service status

## Important Notes

### ⚠️ Disclaimer
- This app is for educational and personal use only
- Use responsibly and respect WhatsApp's terms of service
- The app requires accessibility permissions which should be granted carefully
- Automatic message sending may be subject to WhatsApp's rate limiting

### 🔒 Privacy
- All data is stored locally on your device
- No data is transmitted to external servers
- Messages and contacts remain private

### 🐛 Troubleshooting

**Accessibility Service Not Working:**
- Ensure the service is enabled in Android Settings
- Restart the app after enabling the service
- Check that WhatsApp is installed and updated

**Scheduled Messages Not Sending:**
- Verify exact alarm permissions are granted
- Check battery optimization settings
- Ensure the device is not in deep sleep mode

**Auto Replies Not Working:**
- Confirm accessibility service is active
- Check that auto-reply is enabled in app settings
- Verify WhatsApp notifications are enabled

## Technical Implementation

### Accessibility Service
The core functionality relies on Android's AccessibilityService to:
- Monitor WhatsApp window content changes
- Detect incoming messages and sender information
- Programmatically input text and trigger send actions

### Message Scheduling
Uses Android's AlarmManager and WorkManager for:
- Precise timing of scheduled messages
- Persistent scheduling across device reboots
- Background processing without user intervention

### Database
Room database stores:
- Auto-reply rules with keywords and responses
- Scheduled messages with timing and recurrence settings
- User preferences and app settings

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is provided as-is for educational purposes. Please ensure compliance with local laws and platform terms of service when using this application.

---

**⚡ Quick Start:**
1. Build the APK using Android Studio
2. Install on your Android device
3. Enable accessibility service
4. Create auto-reply rules
5. Schedule messages as needed

Enjoy automated WhatsApp messaging! 🚀