# Android Calendar App

A modern, feature-rich calendar application for Android built with Kotlin, following Material Design principles and Android architecture best practices.

## Features

- **📅 Beautiful Calendar View**: Interactive monthly calendar with date selection
- **📝 Event Management**: Create, edit, and delete events with ease
- **🎨 Color Coding**: Assign colors to events for better organization
- **📍 Location Support**: Add location information to events
- **⏰ Time Management**: Support for both timed events and all-day events
- **💾 Local Storage**: Secure local data storage using Room database
- **🎨 Modern UI**: Material Design 3 components with beautiful animations

## Screenshots

The app features a clean, intuitive interface with:
- Monthly calendar view with event indicators
- Detailed event cards with color coding
- Easy-to-use event creation and editing forms
- Responsive design that works across different screen sizes

## Technical Architecture

### Built With
- **Kotlin** - Primary programming language
- **Android Architecture Components** - ViewModels, LiveData, Room
- **Material Design 3** - Modern UI components and design system
- **Room Database** - Local data persistence
- **ViewBinding** - Type-safe view binding
- **MaterialCalendarView** - Enhanced calendar component

### Architecture Pattern
- **MVVM (Model-View-ViewModel)** - Clean separation of concerns
- **Repository Pattern** - Data access abstraction
- **Observer Pattern** - Reactive UI updates with LiveData

### Project Structure
```
app/src/main/java/com/example/calendarapp/
├── data/
│   ├── Event.kt              # Event entity model
│   ├── EventDao.kt           # Database access object
│   ├── CalendarDatabase.kt   # Room database configuration
│   └── Converters.kt         # Type converters for Room
├── repository/
│   └── EventRepository.kt    # Data repository layer
├── viewmodel/
│   └── CalendarViewModel.kt  # ViewModel for calendar functionality
├── adapter/
│   ├── EventsAdapter.kt      # RecyclerView adapter for events
│   └── ColorAdapter.kt       # Adapter for color selection
├── MainActivity.kt           # Main calendar screen
├── AddEditEventActivity.kt   # Event creation/editing screen
└── EventDetailActivity.kt    # Event details screen
```

## Database Schema

### Event Entity
```kotlin
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTime: Date,
    val endTime: Date,
    val location: String? = null,
    val isAllDay: Boolean = false,
    val color: Int = 0xFF2196F3.toInt(),
    val remindBefore: Long = 0,
    val isRecurring: Boolean = false,
    val recurrencePattern: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
```

## Key Features Implementation

### Calendar Integration
- Custom calendar view with date selection
- Event indicators on calendar dates
- Smooth navigation between months
- Today button for quick navigation

### Event Management
- **Create Events**: Title, description, location, date/time, color
- **Edit Events**: Modify existing event details
- **Delete Events**: Remove events with confirmation
- **All-Day Events**: Toggle between timed and all-day events

### Data Persistence
- Room database for reliable local storage
- Automatic data type conversions
- Efficient querying with LiveData
- Database migration support

### User Experience
- Material Design 3 theming
- Responsive layouts for different screen sizes
- Smooth animations and transitions
- Intuitive navigation patterns

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd CalendarApp
```

2. Open in Android Studio:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. Build and run:
   - Ensure you have Android SDK 24 or higher
   - Build the project
   - Run on device or emulator

## Requirements

- **Minimum SDK**: Android 7.0 (API level 24)
- **Target SDK**: Android 14 (API level 34)
- **Compile SDK**: Android 14 (API level 34)
- **Kotlin**: 1.9.10+
- **Android Gradle Plugin**: 8.2.0+

## Dependencies

### Core Dependencies
- AndroidX Core KTX
- AndroidX AppCompat
- Material Design Components
- ConstraintLayout
- Lifecycle (ViewModel, LiveData)
- Navigation Components

### Database
- Room Runtime
- Room KTX
- Room Compiler (kapt)

### UI Components
- MaterialCalendarView
- RecyclerView
- CardView

### Testing
- JUnit
- AndroidX Test
- Espresso

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Future Enhancements

- [ ] Event notifications and reminders
- [ ] Recurring events support
- [ ] Calendar sync with Google Calendar
- [ ] Week and day view modes
- [ ] Event search functionality
- [ ] Import/export calendar data
- [ ] Dark theme support
- [ ] Widget for home screen
- [ ] Event categories and filtering

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Material Design team for design guidelines
- Prolific Interactive for MaterialCalendarView
- Android team for excellent architecture components