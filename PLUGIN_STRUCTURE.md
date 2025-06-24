# Management and Computer Consultants Cordova Plugin Barcode Scanner Structure

## Overview

This comprehensive barcode scanner plugin for Cordova provides support for a wide range of barcode formats including 1D and 2D barcodes, with modern Android APIs and Google ML Kit integration.

## Repository Information

- **GitHub**: https://github.com/Management-AND-Computer-Consultants/cordova-plugin-barcode-scanner
- **npm Package**: @management-and-computer-consultants/cordova-plugin-barcode-scanner
- **License**: MIT

## File Structure

```
cordova-plugin-barcode-scanner/
├── plugin.xml                 # Plugin configuration
├── package.json              # Plugin metadata and npm configuration
├── .npmignore                # npm ignore rules
├── LICENSE                   # MIT License
├── README.md                 # Documentation
├── PLUGIN_STRUCTURE.md       # This file
├── www/
│   └── BarcodeScanner.js     # JavaScript interface
├── src/
│   └── android/
│       ├── BarcodeScannerPlugin.java      # Main plugin class
│       ├── BarcodeScannerActivity.java    # Camera activity
│       ├── res/
│       │   └── layout/
│       │       └── activity_barcode_scanner.xml
│       └── AndroidManifest.xml
└── example/
    └── index.html            # Usage example
```

## Key Features

- **Comprehensive barcode format support:**
  - 1D formats: Code 11, Code 39, Code 93, Code 128, Codabar, EAN-8, EAN-13, UPC-A, UPC-E, ITF, Industrial 2 of 5, ITF-14
  - 2D formats: QR Code, Data Matrix, PDF417, GS1 DataBar, Maxicode, Micro PDF417, Micro QR, Patch Code, GS1 Composite
  - Special formats: Postal Code, Dot Code, PharmaCode
  - Convenience formats: ALL_1D, ALL_2D, ALL
- CameraX integration for modern camera handling
- Google ML Kit for barcode detection
- Real-time camera preview
- Torch, zoom, and focus controls
- Multiple resolution options
- Permission handling
- Vibration feedback

## API Methods

- `init()` - Initialize scanner
- `scan()` - Single barcode scan
- `startScanning()` - Continuous scanning
- `decode()` - Decode from base64
- `switchTorch()` - Toggle flashlight
- `setZoom()` - Camera zoom control
- `setFocus()` - Focus point control
- `getResolution()` - Get current resolution
- `hasCamera()` - Check device camera
- `requestPermissions()` - Request camera permissions
- `checkPermissions()` - Check permission status
- `destroy()` - Cleanup resources

## Dependencies

- androidx.core:core:1.6.0
- androidx.camera:camera-core:1.2.0
- androidx.camera:camera-camera2:1.2.0
- androidx.camera:camera-lifecycle:1.2.0
- androidx.camera:camera-view:1.2.0
- com.google.android.gms:play-services-mlkit-barcode-scanning:17.2.0

## Target SDK

- Minimum: API 21 (Android 5.0)
- Target: API 35 (Android 15)
- Cordova Android: 10.0.0+ 