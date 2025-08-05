# Android Calendar App

A modern, feature-rich Android calendar application built with Kotlin and Material Design 3.

## Features

### 📅 Calendar View
- Monthly calendar grid with intuitive navigation
- Today highlighting and date selection
- Event indicators on days with scheduled events
- Smooth month-to-month navigation

### 📝 Event Management
- Create, edit, and delete events
- Set event title, description, location, and time
- Configure custom reminders (5 min, 15 min, 30 min, 1 hour, 1 day)
- Color-coded events for easy identification

### 🎨 Modern UI/UX
- Material Design 3 components
- Dark/Light theme support
- Responsive layout for different screen sizes
- Smooth animations and transitions

### 💾 Data Persistence
- Room database for local event storage
- Offline functionality
- Data backup and restore capabilities

### 🔔 Notifications
- Event reminders with customizable timing
- Background notification scheduling
- Rich notification content

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Data Layer**: Room database with DAOs and repositories
- **Domain Layer**: Use cases and business logic
- **Presentation Layer**: Activities, Fragments, and ViewModels
- **UI Layer**: XML layouts and Material Design components

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Android Views with ViewBinding
- **Architecture**: MVVM with Repository pattern
- **Database**: Room with Kotlin Coroutines
- **Navigation**: Navigation Component
- **UI Components**: Material Design 3
- **Async Operations**: Kotlin Coroutines and Flow
- **Dependency Injection**: Manual dependency injection

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/calendarapp/
│   │   ├── data/                 # Data layer
│   │   │   ├── Event.kt         # Event entity
│   │   │   ├── EventDao.kt      # Data access object
│   │   │   └── AppDatabase.kt   # Room database
│   │   ├── repository/          # Repository layer
│   │   │   └── EventRepository.kt
│   │   ├── ui/                  # Presentation layer
│   │   │   ├── calendar/        # Calendar feature
│   │   │   ├── events/          # Events list feature
│   │   │   └── settings/        # Settings feature
│   │   └── receiver/            # Broadcast receivers
│   └── res/                     # Resources
│       ├── layout/              # XML layouts
│       ├── drawable/            # Vector drawables
│       ├── values/              # Colors, strings, themes
│       └── navigation/          # Navigation graphs
```

## Key Components

### Calendar Fragment
- Displays monthly calendar grid
- Handles date selection and navigation
- Shows events for selected dates
- Integrates with event management

### Event Management
- Add/Edit event dialog with form validation
- Date and time pickers
- Reminder configuration
- Color selection for events

### Database Layer
- Room database with TypeConverters for LocalDateTime
- Efficient queries for date-based event retrieval
- Support for event search and filtering

## Setup and Installation

1. **Prerequisites**
   - Android Studio Arctic Fox or later
   - Android SDK API 24+ (Android 7.0)
   - Kotlin 1.9.10+

2. **Build Configuration**
   ```gradle
   compileSdk 34
   minSdk 24
   targetSdk 34
   ```

3. **Dependencies**
   - AndroidX Core KTX
   - Material Design 3
   - Room Database
   - Navigation Component
   - Lifecycle Components
   - Kotlin Coroutines

4. **Permissions**
   - READ_CALENDAR
   - WRITE_CALENDAR
   - SCHEDULE_EXACT_ALARM

## Usage

### Adding Events
1. Tap the floating action button (+)
2. Fill in event details (title, description, location)
3. Select date and time using pickers
4. Choose reminder timing
5. Save the event

### Navigating Calendar
1. Use arrow buttons to navigate between months
2. Tap "Today" button to return to current date
3. Tap any date to view events for that day
4. Tap events to edit or delete them

### Viewing Events
1. Navigate to "Events" tab to see all events
2. Events are sorted by date and time
3. Use search functionality to find specific events

## Customization

### Themes
The app supports Material Design 3 theming:
- Light theme (default)
- Dark theme
- System theme (follows device setting)

### Colors
Event colors can be customized:
- 8 predefined color options
- Material Design color palette
- Custom color support

### Reminders
Configurable reminder options:
- No reminder
- 5 minutes before
- 15 minutes before
- 30 minutes before
- 1 hour before
- 1 day before

## Future Enhancements

- [ ] Google Calendar integration
- [ ] Recurring events
- [ ] Event categories and tags
- [ ] Calendar sharing
- [ ] Widget support
- [ ] Cloud sync
- [ ] Advanced search filters
- [ ] Event templates
- [ ] Location-based reminders
- [ ] Multi-language support

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue on GitHub
- Check the documentation
- Review the code comments

---

Built with ❤️ using modern Android development practices.