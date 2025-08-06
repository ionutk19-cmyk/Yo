#!/bin/bash

echo "Building WhatsApp Auto Responder APK..."

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ]; then
    echo "ANDROID_HOME is not set. Please set it to your Android SDK path."
    exit 1
fi

# Set up environment
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Create build directory
mkdir -p app/build/outputs/apk/debug

# Compile resources
echo "Compiling resources..."
aapt2 compile --dir app/src/main/res -o app/build/intermediates/aapt2/debug

# Compile Java/Kotlin code
echo "Compiling source code..."
kotlinc app/src/main/java/com/whatsapp/auto/responder/*.kt \
    -cp "$ANDROID_HOME/platforms/android-34/android.jar" \
    -d app/build/intermediates/javac/debug/classes

# Create APK
echo "Creating APK..."
aapt2 link app/build/intermediates/aapt2/debug/*.flat \
    -I "$ANDROID_HOME/platforms/android-34/android.jar" \
    -o app/build/outputs/apk/debug/app-debug.apk

echo "Build completed! APK is at: app/build/outputs/apk/debug/app-debug.apk"