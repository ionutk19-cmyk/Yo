# 📱 Android Calendar App - Build Instructions

## 🚀 Quick Build (Recommended)

### Prerequisites
1. **Android Studio** (latest version)
2. **Android SDK** (API level 24+)
3. **Java 8+** (OpenJDK or Oracle JDK)

### Step-by-Step Build

#### Method 1: Using Android Studio (Easiest)
1. **Open Project**
   ```bash
   # Open Android Studio
   # File → Open → Select the project folder
   ```

2. **Sync Project**
   - Click "Sync Now" when prompted
   - Or go to File → Sync Project with Gradle Files

3. **Build APK**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or use the build script: `./build-apk.sh`

#### Method 2: Command Line
```bash
# Make sure you're in the project root
cd /path/to/calendar-app

# Set Android SDK path (if not already set)
export ANDROID_HOME=/path/to/Android/Sdk

# Run the build script
./build-apk.sh

# Or build manually
./gradlew assembleDebug
```

## 📦 APK Location
After successful build, the APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 Troubleshooting

### Common Issues

#### 1. ANDROID_HOME not set
```bash
export ANDROID_HOME=/path/to/Android/Sdk
```

#### 2. Java not found
```bash
# Install OpenJDK
sudo apt update
sudo apt install openjdk-11-jdk

# Or on macOS
brew install openjdk@11
```

#### 3. Gradle wrapper not executable
```bash
chmod +x gradlew
```

#### 4. Build fails with SDK errors
- Open Android Studio
- Go to SDK Manager
- Install Android SDK Platform 34
- Install Android SDK Build-Tools

## 📱 Installing the APK

### On Device
1. Enable "Unknown Sources" in Settings
2. Transfer APK to device
3. Open APK file and install

### Using ADB
```bash
# Connect device via USB with USB debugging enabled
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🏗️ Build Variants

### Debug APK (Default)
```bash
./gradlew assembleDebug
```

### Release APK (Signed)
```bash
./gradlew assembleRelease
```

### Bundle (Google Play Store)
```bash
./gradlew bundleRelease
```

## 🔐 Signing the APK

For release builds, you need to sign the APK:

1. **Generate Keystore**
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure signing in app/build.gradle**
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("my-release-key.keystore")
               storePassword "your-store-password"
               keyAlias "my-key-alias"
               keyPassword "your-key-password"
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

## 📋 System Requirements

### Development
- **OS**: Windows 10+, macOS 10.14+, or Linux
- **RAM**: 8GB+ (16GB recommended)
- **Storage**: 10GB+ free space
- **Java**: OpenJDK 11 or Oracle JDK 11+

### Target Device
- **Android**: API level 24+ (Android 7.0+)
- **RAM**: 2GB+ recommended
- **Storage**: 50MB+ free space

## 🎯 Features Included

✅ **Monthly Calendar View**
✅ **Event Management** (Create, Edit, Delete)
✅ **Modern Material Design 3 UI**
✅ **Date/Time Pickers**
✅ **All-Day Events**
✅ **Reminder Settings**
✅ **Permission Handling**
✅ **Responsive Design**

## 📞 Support

If you encounter issues:

1. **Check the logs**: `./gradlew assembleDebug --info`
2. **Clean and rebuild**: `./gradlew clean assembleDebug`
3. **Update dependencies**: Sync project in Android Studio
4. **Check SDK installation**: Verify Android SDK is properly installed

## 🚀 Next Steps

After building the APK:

1. **Test on device** - Install and test all features
2. **Customize** - Modify colors, themes, and features
3. **Add database** - Implement Room database for persistence
4. **Add notifications** - Implement reminder notifications
5. **Publish** - Upload to Google Play Store

---

**Happy Building! 🎉**