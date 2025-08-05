# Android Calendar App

A modern, feature-rich calendar application for Android built with Kotlin and Material Design 3.

## Features

- **Monthly Calendar View**: Navigate through months with a clean, intuitive interface
- **Event Management**: Create, edit, and delete calendar events
- **Event Details**: View comprehensive event information including title, description, location, and time
- **All-Day Events**: Support for all-day events
- **Reminders**: Set custom reminders for events (15 min, 30 min, 1 hour, 1 day, 1 week)
- **Modern UI**: Material Design 3 components with beautiful animations
- **Permission Handling**: Proper calendar permissions management
- **Responsive Design**: Works on various screen sizes

## Screenshots

The app includes:
- Main calendar view with month navigation
- Event list for selected dates
- Add/Edit event screen with date/time pickers
- Event detail view
- Modern Material Design 3 interface

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/calendarapp/
│   │   ├── MainActivity.kt          # Main calendar view
│   │   ├── AddEventActivity.kt      # Add/Edit events
│   │   ├── EventDetailActivity.kt   # View event details
│   │   ├── CalendarAdapter.kt       # Calendar grid adapter
│   │   ├── EventAdapter.kt          # Event list adapter
│   │   └── Event.kt                 # Event data class
│   ├── res/
│   │   ├── layout/                  # UI layouts
│   │   ├── values/                  # Strings, colors, themes
│   │   └── menu/                    # Menu resources
│   └── AndroidManifest.xml
├── build.gradle                     # App dependencies
└── proguard-rules.pro
```

## Key Components

### MainActivity
- Displays the monthly calendar grid
- Handles date selection and navigation
- Shows events for selected dates
- Manages calendar permissions

### CalendarAdapter
- Renders the calendar grid with proper month layout
- Handles day selection and highlighting
- Shows event indicators on days with events

### EventAdapter
- Displays events in a list format
- Shows event details with color coding
- Handles event item interactions

### AddEventActivity
- Comprehensive event creation/editing interface
- Date and time picker dialogs
- Form validation and data persistence
- Reminder settings

## Dependencies

The app uses the following key dependencies:
- **AndroidX Core KTX**: Kotlin extensions
- **Material Design 3**: Modern UI components
- **RecyclerView**: For calendar grid and event lists
- **ViewBinding**: Type-safe view access
- **Room Database**: For data persistence (ready for implementation)

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (API level 24)
- Kotlin 1.9.10+

### Installation

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run on an Android device or emulator

### Building

```bash
# Build the project
./gradlew build

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

## Usage

### Calendar Navigation
- Use the arrow buttons to navigate between months
- Tap on any day to select it and view events
- Today's date is highlighted with a special background

### Adding Events
1. Tap the floating action button (+)
2. Fill in event details (title, description, location)
3. Set start and end dates/times
4. Choose reminder settings
5. Tap "Save" to create the event

### Managing Events
- Tap on any event in the list to view/edit details
- Use the edit button to modify event information
- Delete events using the menu option in edit mode

### Permissions
The app requests calendar permissions on first launch to:
- Read existing calendar events
- Create new calendar events
- Modify calendar events

## Customization

### Colors
Edit `app/src/main/res/values/colors.xml` to customize the app's color scheme:

```xml
<color name="primary">#FF2196F3</color>
<color name="accent">#FFFF5722</color>
```

### Themes
Modify `app/src/main/res/values/themes.xml` to change the app's appearance:

```xml
<style name="Theme.CalendarApp" parent="Theme.Material3.DayNight.NoActionBar">
    <!-- Customize theme attributes -->
</style>
```

## Future Enhancements

- **Database Integration**: Implement Room database for persistent storage
- **Calendar Sync**: Integrate with device calendar
- **Notifications**: Push notifications for event reminders
- **Recurring Events**: Support for repeating events
- **Event Categories**: Color-coded event categories
- **Search**: Search functionality for events
- **Export/Import**: Calendar data export/import
- **Widget**: Home screen calendar widget

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the documentation
- Review the code comments

---

**Note**: This is a demo implementation. For production use, consider adding:
- Proper error handling
- Database integration
- Unit tests
- UI tests
- Accessibility features
- Performance optimizations