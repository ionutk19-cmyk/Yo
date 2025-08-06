#!/bin/bash

GRADLE_VERSION="8.0"
GRADLE_HOME="$HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION-bin"

if [ ! -d "$GRADLE_HOME" ]; then
    echo "Downloading Gradle $GRADLE_VERSION..."
    mkdir -p "$GRADLE_HOME"
    cd "$GRADLE_HOME"
    wget -q "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
    unzip -q "gradle-$GRADLE_VERSION-bin.zip"
    mv gradle-$GRADLE_VERSION/* .
    rm -rf gradle-$GRADLE_VERSION gradle-$GRADLE_VERSION-bin.zip
fi

export PATH="$GRADLE_HOME/bin:$PATH"
exec gradle "$@"