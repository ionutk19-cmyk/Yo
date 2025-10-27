#!/bin/bash

# Android Calendar App Build Script
# This script builds the APK for the calendar app

echo "🔨 Building Android Calendar App APK..."

# Check if ANDROID_HOME is set
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ ANDROID_HOME is not set. Please set it to your Android SDK location."
    echo "Example: export ANDROID_HOME=/path/to/Android/Sdk"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    exit 1
fi

# Check if the project structure is correct
if [ ! -f "app/build.gradle" ]; then
    echo "❌ Project structure is incorrect. Make sure you're in the project root."
    exit 1
fi

echo "✅ Project structure looks good"
echo "✅ Java is available: $(java -version 2>&1 | head -1)"

# Make gradlew executable
chmod +x gradlew

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo "🔨 Building debug APK..."
./gradlew assembleDebug

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "📱 APK location: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📋 To install on a connected device:"
    echo "   ./gradlew installDebug"
    echo ""
    echo "📋 To build release APK:"
    echo "   ./gradlew assembleRelease"
else
    echo "❌ Build failed!"
    exit 1
fi