#!/bin/bash

# Android Calendar App - Setup and Build Script
# This script helps set up the environment and build the APK

set -e  # Exit on any error

echo "📱 Android Calendar App - Setup and Build"
echo "=========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in the right directory
if [ ! -f "app/build.gradle" ]; then
    print_error "This script must be run from the project root directory"
    exit 1
fi

print_status "Checking system requirements..."

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
    print_success "Java found: $JAVA_VERSION"
else
    print_error "Java is not installed. Please install OpenJDK 11 or later."
    echo "Ubuntu/Debian: sudo apt install openjdk-11-jdk"
    echo "macOS: brew install openjdk@11"
    echo "Windows: Download from https://adoptium.net/"
    exit 1
fi

# Check Android SDK
if [ -z "$ANDROID_HOME" ]; then
    print_warning "ANDROID_HOME is not set"
    echo "Please set ANDROID_HOME to your Android SDK location:"
    echo "export ANDROID_HOME=/path/to/Android/Sdk"
    echo ""
    echo "Common locations:"
    echo "  Linux: /home/username/Android/Sdk"
    echo "  macOS: /Users/username/Library/Android/sdk"
    echo "  Windows: C:\\Users\\username\\AppData\\Local\\Android\\Sdk"
    echo ""
    read -p "Do you want to continue without Android SDK? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    print_success "Android SDK found at: $ANDROID_HOME"
fi

# Make gradlew executable
print_status "Setting up Gradle wrapper..."
chmod +x gradlew

# Check if gradle wrapper jar exists
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    print_warning "Gradle wrapper JAR not found. Downloading..."
    mkdir -p gradle/wrapper
    wget -O gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.0.0/gradle/wrapper/gradle-wrapper.jar
    if [ $? -eq 0 ]; then
        print_success "Gradle wrapper JAR downloaded successfully"
    else
        print_error "Failed to download Gradle wrapper JAR"
        exit 1
    fi
fi

# Clean previous builds
print_status "Cleaning previous builds..."
./gradlew clean

# Try to build
print_status "Building APK..."
if ./gradlew assembleDebug; then
    print_success "Build completed successfully!"
    echo ""
    echo "📦 APK Location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📱 To install on a connected device:"
    echo "   ./gradlew installDebug"
    echo ""
    echo "🔧 To build release version:"
    echo "   ./gradlew assembleRelease"
    echo ""
    
    # Check if APK was created
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
        print_success "APK created successfully! Size: $APK_SIZE"
        
        echo ""
        echo "🎉 Your Android Calendar App is ready!"
        echo ""
        echo "📋 Next steps:"
        echo "   1. Transfer the APK to your Android device"
        echo "   2. Enable 'Unknown Sources' in device settings"
        echo "   3. Install the APK"
        echo "   4. Grant calendar permissions when prompted"
        echo ""
        echo "📱 App Features:"
        echo "   ✅ Monthly calendar view"
        echo "   ✅ Create, edit, and delete events"
        echo "   ✅ Modern Material Design 3 UI"
        echo "   ✅ Date and time pickers"
        echo "   ✅ All-day events support"
        echo "   ✅ Reminder settings"
        echo "   ✅ Permission handling"
    else
        print_warning "Build completed but APK not found"
    fi
else
    print_error "Build failed!"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "   1. Make sure Android SDK is properly installed"
    echo "   2. Check that ANDROID_HOME is set correctly"
    echo "   3. Try: ./gradlew clean assembleDebug --info"
    echo "   4. Open project in Android Studio for detailed error messages"
    exit 1
fi

echo ""
print_success "Setup and build process completed!"