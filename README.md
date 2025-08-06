## WhatsAutoReply

This repository contains a **proof-of-concept** Android application that can automatically reply to incoming WhatsApp messages and schedule WhatsApp messages to be sent at a future time that you specify.

**⚠️  Important legal & ethical notice**

Automating WhatsApp messages violates WhatsApp’s Terms of Service. Your account can be **banned** or **limited** if you use automation in production against their policies. This repository is provided **for educational purposes only** and **must not** be used on a production account or distributed through Google Play.

---

### Features

1. **Auto-reply to incoming WhatsApp messages**  
   Implemented with a `NotificationListenerService` that listens for new WhatsApp notifications and sends a quick-reply back to the sender.
2. **Scheduled messages**  
   A simple UI lets you compose a message and choose a date & time.  
   Under the hood an `AlarmManager` schedules a `BroadcastReceiver` which launches WhatsApp with the text pre-filled and (optionally) auto-sends the message via Accessibility.

### High-level architecture

```
┌────────────┐          ┌───────────────────────┐
│ MainActivity│◄────────► Room DB (schedules)   │
└─────┬──────┘          └───────────────────────┘
      │
      ▼
┌────────────┐   intents   ┌─────────────────────┐
│ AlarmManager│────────────► ScheduleReceiver    │
└────────────┘             └─────────┬───────────┘
                                      │
                                      ▼
                           ┌─────────────────────┐
                           │ AccessibilityHelper │
                           └─────────────────────┘
```

### Building the APK locally

1. Install **Android Studio Hedgehog** (or newer) and Android SDK _33_ or later.
2. Clone the repository

   ```bash
   git clone <this-repo>
   cd whatsautoreply
   ```
3. Open the project in Android Studio and press **Run ▶**.

> The Gradle wrapper (`./gradlew`) is included, so you can also build head-less:
>
> ```bash
> ./gradlew assembleDebug
> ```
>
> The APK will be in `app/build/outputs/apk/debug/app-debug.apk`.

### App signing & distribution

This project ships with **debug signing keys only**. To install the APK on a real device, enable **USB debugging** and run `adb install`.  For Play-Store distribution you **must** replace the debug key with your own **release keystore**.

### Minimal code overview

* **`MainActivity.kt`** – simple UI to configure the auto-reply text and schedule messages.
* **`AutoReplyNotificationListener.kt`** – `NotificationListenerService` that listens for WhatsApp notifications and replies automatically.
* **`ScheduleReceiver.kt`** – `BroadcastReceiver` triggered by `AlarmManager` to send the scheduled message.
* **`AccessibilitySenderService.kt`** – optional `AccessibilityService` that actually presses the *send* button in WhatsApp.

See the individual source files for inline documentation.

### Disclaimer

Using or distributing automation that interacts with WhatsApp may break their **Terms of Service**.  This repository is for **educational use only**—use it at your own risk.