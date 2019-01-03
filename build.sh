#!/bin/bash

# Exit on error
set -e

# Work off travis
if [[ ! -z TRAVIS_PULL_REQUEST ]]; then
  echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
else
  echo "TRAVIS_PULL_REQUEST: unset, setting to false"
  TRAVIS_PULL_REQUEST=false
fi

# Install preview deps
yes | ${ANDROID_HOME}/tools/bin/sdkmanager --channel=3 \
  "tools" "platform-tools" "build-tools;28.0.3" "platforms;android-28"

# Build
if [ $TRAVIS_PULL_REQUEST = false ] ; then
  echo "Building full project"
  # For a merged commit, build all configurations.
  ./gradlew clean ktlint build
else
  # On a pull request, just build debug which is much faster and catches
  # obvious errors.
  ./gradlew clean ktlint assembleDebug check
fi