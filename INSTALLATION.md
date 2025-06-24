# Installation Guide

## Prerequisites

- Cordova CLI installed
- Android SDK with API level 21 or higher
- Android Studio (recommended for development)

## Installation Steps

### 1. Add the Plugin

```bash
cordova plugin add management-and-computer-consultants-cordova-plugin-barcode-scanner
```

### 2. Configure Android Build

Ensure your project's `platforms/android/build.gradle` includes the Google Maven repository:

```gradle
allprojects {
    repositories {
        google()  // Required for ML Kit dependencies
        mavenCentral()
        // ... other repositories
    }
}
```

### 3. Set Minimum SDK Version

Add to your project's `config.xml`:

```xml
<preference name="android-minSdkVersion" value="21" />
```

### 4. Build the Project

```bash
cordova build android
```

## Troubleshooting

### ML Kit Dependency Error

If you see an error like:
```
Could not find com.google.mlkit:barcode-scanning:16.2.0
```

**Solution:**
1. Ensure the Google Maven repository is added to your `build.gradle`
2. Clean and rebuild the project:
   ```bash
   cordova clean android
   cordova build android
   ```

**Note:** This plugin uses ML Kit barcode scanning version 16.2.0, which is specifically chosen for compatibility with Ionic 1 AngularJS projects.

### Permission Issues

If camera permissions are not working:
1. Check that the plugin is properly installed
2. Ensure the app requests camera permissions at runtime
3. Verify the permissions are declared in the Android manifest

### Build Errors

If you encounter other build errors:
1. Update your Android SDK tools
2. Ensure you have the latest Cordova CLI
3. Check that all dependencies are compatible

### AndroidManifest.xml Conflict

If you see an error like:
```
"AndroidManifest.xml" already exists!
```

**Solution:**
1. Remove the plugin completely:
   ```bash
   cordova plugin remove management-and-computer-consultants-cordova-plugin-barcode-scanner
   ```
2. Clean the project:
   ```bash
   cordova clean android
   ```
3. Re-add the plugin:
   ```bash
   cordova plugin add management-and-computer-consultants-cordova-plugin-barcode-scanner
   ```

This error typically occurs when there's a conflict with existing manifest files and is resolved by a clean reinstall.

## Verification

To verify the plugin is working:

1. Add the plugin to your project
2. Build the project successfully
3. Test the barcode scanning functionality in your app

## Support

If you continue to experience issues, please check:
- Cordova version compatibility
- Android SDK version
- Plugin dependencies
- Project configuration 