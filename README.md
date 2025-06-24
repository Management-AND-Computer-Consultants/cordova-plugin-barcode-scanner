# Management and Computer Consultants Cordova Plugin Barcode Scanner

A comprehensive barcode scanner plugin for Cordova supporting multiple 1D and 2D barcode formats with modern API.

## Features

- **Comprehensive barcode format support** including 1D and 2D formats
- **Modern API** with flexible configuration options
- **CameraX integration** for modern Android camera handling
- **Google ML Kit** for accurate barcode detection
- **Real-time camera preview** with customizable detection area
- **Audio and haptic feedback** options
- **Multiple resolution support** from 480P to 4K
- **Torch and zoom controls** for enhanced scanning

## Installation

### Via npm (Recommended)

```bash
npm install @management-and-computer-consultants/cordova-plugin-barcode-scanner
```

### Via Cordova CLI

```bash
cordova plugin add @management-and-computer-consultants/cordova-plugin-barcode-scanner
```

### Manual Installation

```bash
cordova plugin add https://github.com/Management-AND-Computer-Consultants/cordova-plugin-barcode-scanner.git
```

## Requirements

- Cordova 9.0.0 or higher
- Cordova Android 10.0.0 or higher
- Android SDK 35 (API Level 35) or higher

## Features

- **Data Matrix Focus**: Optimized for Data Matrix barcode scanning
- **Real-time Detection**: Live camera preview with instant barcode recognition
- **Camera Controls**: Torch/flashlight, zoom, and focus controls
- **Multiple Resolutions**: Support for various camera resolutions
- **Permission Handling**: Automatic camera permission requests
- **Modern UI**: Clean, user-friendly scanning interface
- **Vibration Feedback**: Haptic feedback when barcode is detected

## Supported Barcode Formats

- **Data Matrix** (Primary focus)
- QR Code
- Code 128
- Code 39
- EAN-13
- EAN-8
- UPC-A
- UPC-E

## Usage

### Basic Scanning

```javascript
// Initialize the scanner
cordova.plugins.mccbarcode.barcodeScanner.init('', function() {
    console.log('Scanner initialized');
}, function(error) {
    console.error('Initialization failed:', error);
});

// Scan with default options
cordova.plugins.mccbarcode.barcodeScanner.scan({}, function(result) {
    console.log('Barcode detected:', result.text);
    console.log('Format:', result.format);
}, function(error) {
    console.error('Scan failed:', error);
});
```

### Modern API Usage

```javascript
// Define scanning options
const defaultOptions = {
    barcodeFormats: {
        Code128: true,
        Code39: true,
        Code93: true,
        CodaBar: true,
        DataMatrix: true,
        EAN13: true,
        EAN8: true,
        ITF: true,
        QRCode: true,
        UPCA: true,
        UPCE: true,
        PDF417: true,
        Aztec: true,
    },
    beepOnSuccess: false,
    vibrateOnSuccess: false,
    detectorSize: 0.6,
    rotateCamera: false,
};

// Scan with custom options
cordova.plugins.mccbarcode.barcodeScanner.scan(
    defaultOptions,
    (result) => {
        // Do something with the data
        console.log("result : ", result);
        console.log("Barcode text: ", result.text);
        console.log("Barcode format: ", result.format);
    },
    (error) => {
        alert("Scanning failed: " + error);
    }
);
```

### Scanning Different Barcode Formats

```javascript
// Scan for specific formats only
cordova.plugins.mccbarcode.barcodeScanner.scan({
    barcodeFormats: {
        QRCode: true,
        DataMatrix: true,
        Code128: false,
        Code39: false
    },
    beepOnSuccess: true,
    vibrateOnSuccess: true
}, function(result) {
    console.log('Detected:', result.text, 'Format:', result.format);
}, function(error) {
    console.error('Scan failed:', error);
});

// Scan for retail barcodes
cordova.plugins.mccbarcode.barcodeScanner.scan({
    barcodeFormats: {
        EAN13: true,
        EAN8: true,
        UPCA: true,
        UPCE: true
    },
    detectorSize: 0.8,
    torch: true
}, function(result) {
    console.log('Retail barcode:', result.text);
}, function(error) {
    console.error('Scan failed:', error);
});

// Scan for industrial barcodes
cordova.plugins.mccbarcode.barcodeScanner.scan({
    barcodeFormats: {
        Code128: true,
        Code39: true,
        ITF: true
    },
    beepOnSuccess: true,
    vibrateOnSuccess: true
}, function(result) {
    console.log('Industrial barcode:', result.text);
}, function(error) {
    console.error('Scan failed:', error);
});
```

### Advanced Scanning Options

```javascript
// Custom scanning options
cordova.plugins.mccbarcode.barcodeScanner.scan({
    barcodeFormats: {
        QRCode: true,
        DataMatrix: true,
        Code128: true
    },
    resolution: '1080P',
    torch: true,
    beepOnSuccess: true,
    vibrateOnSuccess: true,
    detectorSize: 0.8,
    timeout: 60000
}, function(result) {
    // Handle successful scan
    alert('Detected: ' + result.text);
}, function(error) {
    // Handle error
    alert('Scan failed: ' + error);
});
```

### Continuous Scanning

```javascript
// Start continuous scanning
cordova.plugins.mccbarcode.barcodeScanner.startScanning({
    barcodeFormats: {
        QRCode: true,
        DataMatrix: true
    },
    beepOnSuccess: true,
    vibrateOnSuccess: true
}, function(result) {
    // Called each time a barcode is detected
    console.log('Continuous scan result:', result);
}, function(error) {
    console.error('Continuous scan error:', error);
});

// Stop continuous scanning
cordova.plugins.mccbarcode.barcodeScanner.stopScanning(function() {
    console.log('Scanning stopped');
}, function(error) {
    console.error('Error stopping scan:', error);
});
```

### Camera Controls

```javascript
// Toggle torch/flashlight
cordova.plugins.mccbarcode.barcodeScanner.switchTorch(true, function() {
    console.log('Torch turned on');
}, function(error) {
    console.error('Torch error:', error);
});

// Set camera zoom
cordova.plugins.mccbarcode.barcodeScanner.setZoom(2.0, function() {
    console.log('Zoom set to 2x');
}, function(error) {
    console.error('Zoom error:', error);
});

// Set focus point
cordova.plugins.mccbarcode.barcodeScanner.setFocus({x: 0.5, y: 0.5}, function() {
    console.log('Focus set to center');
}, function(error) {
    console.error('Focus error:', error);
});
```

### Permission Handling

```javascript
// Check camera permissions
cordova.plugins.mccbarcode.barcodeScanner.checkPermissions(function(hasPermission) {
    if (hasPermission) {
        console.log('Camera permission granted');
    } else {
        console.log('Camera permission denied');
    }
}, function(error) {
    console.error('Permission check failed:', error);
});

// Request camera permissions
cordova.plugins.mccbarcode.barcodeScanner.requestPermissions(function() {
    console.log('Permissions granted');
}, function(error) {
    console.error('Permission request failed:', error);
});
```

### Utility Methods

```javascript
// Check if device has camera
cordova.plugins.mccbarcode.barcodeScanner.hasCamera(function(hasCamera) {
    if (hasCamera) {
        console.log('Device has camera');
    } else {
        console.log('Device does not have camera');
    }
}, function(error) {
    console.error('Camera check failed:', error);
});

// Get current camera resolution
cordova.plugins.mccbarcode.barcodeScanner.getResolution(function(resolution) {
    console.log('Current resolution:', resolution); // e.g., "1280x720"
}, function(error) {
    console.error('Resolution check failed:', error);
});

// Decode barcode from base64 image
cordova.plugins.mccbarcode.barcodeScanner.decode(base64ImageData, {
    barcodeFormats: {
        QRCode: true,
        DataMatrix: true
    }
}, function(result) {
    console.log('Decoded barcode:', result);
}, function(error) {
    console.error('Decode failed:', error);
});
```

## API Reference

### cordova.plugins.mccbarcode.barcodeScanner Object

#### Default Options

```javascript
BarcodeScanner.DefaultOptions = {
    barcodeFormats: {
        Code128: true,
        Code39: true,
        Code93: true,
        CodaBar: true,
        DataMatrix: true,
        EAN13: true,
        EAN8: true,
        ITF: true,
        QRCode: true,
        UPCA: true,
        UPCE: true,
        PDF417: true,
        Aztec: true,
        // Additional formats
        Code11: false,
        Industrial2Of5: false,
        ITF14: false,
        GS1DataBar: false,
        Maxicode: false,
        MicroPDF417: false,
        MicroQR: false,
        PatchCode: false,
        GS1Composite: false,
        PostalCode: false,
        DotCode: false,
        PharmaCode: false
    },
    beepOnSuccess: false,
    vibrateOnSuccess: false,
    detectorSize: 0.6,
    rotateCamera: false,
    torch: false,
    resolution: 'AUTO', // AUTO, 480P, 720P, 1080P, 2K, 4K
    timeout: 30000
};
```

#### Scanning Options

- `barcodeFormats` - Object specifying which barcode formats to scan for
- `beepOnSuccess` - Play beep sound on successful scan (boolean)
- `vibrateOnSuccess` - Vibrate device on successful scan (boolean)
- `detectorSize` - Size of detection area (0.0 to 1.0, default: 0.6)
- `rotateCamera` - Rotate camera 180 degrees (boolean)
- `torch` - Enable torch/flashlight (boolean)
- `resolution` - Camera resolution (string: AUTO, 480P, 720P, 1080P, 2K, 4K)
- `timeout` - Scan timeout in milliseconds (number)

#### Supported Barcode Formats

**1D Barcode Formats:**
- `Code11` - Code 11 format
- `Code39` - Code 39 format
- `Code93` - Code 93 format
- `Code128` - Code 128 format
- `CodaBar` - Codabar format
- `EAN8` - EAN-8 format
- `EAN13` - EAN-13 format
- `UPCA` - UPC-A format
- `UPCE` - UPC-E format
- `ITF` - Interleaved 2 of 5 (ITF)
- `Industrial2Of5` - Industrial 2 of 5
- `ITF14` - ITF-14 format

**2D Barcode Formats:**
- `QRCode` - QR Code format
- `DataMatrix` - Data Matrix format
- `PDF417` - PDF417 format
- `GS1DataBar` - GS1 DataBar format
- `Maxicode` - Maxicode format
- `MicroPDF417` - Micro PDF417 format
- `MicroQR` - Micro QR format
- `PatchCode` - Patch Code format
- `GS1Composite` - GS1 Composite format

**Special Formats:**
- `PostalCode` - Postal Code format
- `DotCode` - Dot Code format
- `PharmaCode` - PharmaCode format
- `Aztec` - Aztec format

#### Methods

- `init(license, successCallback, errorCallback)` - Initialize the scanner
- `scan(options, successCallback, errorCallback)` - Scan for barcodes
- `decode(base64Data, options, successCallback, errorCallback)` - Decode from base64
- `startScanning(options, onScanned, errorCallback)` - Start continuous scanning
- `stopScanning(successCallback, errorCallback)` - Stop continuous scanning
- `pauseScanning(successCallback, errorCallback)` - Pause scanning
- `resumeScanning(successCallback, errorCallback)` - Resume scanning
- `switchTorch(enabled, successCallback, errorCallback)` - Toggle torch
- `setZoom(zoomFactor, successCallback, errorCallback)` - Set camera zoom
- `setFocus(point, successCallback, errorCallback)` - Set focus point
- `getResolution(successCallback, errorCallback)` - Get current resolution
- `hasCamera(successCallback, errorCallback)` - Check if device has camera
- `requestPermissions(successCallback, errorCallback)` - Request permissions
- `checkPermissions(successCallback, errorCallback)` - Check permissions
- `destroy(successCallback, errorCallback)` - Destroy scanner instance

#### Result Object

The success callback receives a result object with the following properties:

```javascript
{
    text: "barcode content",
    format: "detected format name",
    success: true
}
```

## Error Handling

The plugin provides comprehensive error handling for various scenarios:

- Camera permission denied
- Device doesn't have camera
- Scanner not initialized
- Scan timeout
- Camera initialization failure
- ML Kit processing errors

## Dependencies

### Android Dependencies

- `androidx.core:core:1.6.0`
- `androidx.camera:camera-core:1.2.0`
- `androidx.camera:camera-camera2:1.2.0`
- `androidx.camera:camera-lifecycle:1.2.0`
- `androidx.camera:camera-view:1.2.0`
- `com.google.android.gms:play-services-mlkit-barcode-scanning:17.2.0`

## Building and Testing

### Build the Plugin

```bash
# Navigate to plugin directory
cd management-and-computer-consultants-cordova-plugin-barcode-scanner

# Install dependencies (if any)
npm install

# Build the plugin
cordova prepare
```

### Test in a Cordova Project

```bash
# Create a new Cordova project
cordova create test-project
cd test-project

# Add Android platform
cordova platform add android

# Add the plugin
cordova plugin add https://github.com/Management-AND-Computer-Consultants/cordova-plugin-barcode-scanner.git

# Build and run
cordova build android
cordova run android
```

## Troubleshooting

### Common Issues

1. **Camera Permission Denied**
   - Ensure the app has camera permissions
   - Use `BarcodeScanner.requestPermissions()` to request permissions

2. **Scanner Not Initialized**
   - Call `BarcodeScanner.init()` before scanning
   - Check for initialization errors

3. **No Barcode Detected**
   - Ensure the barcode is clearly visible
   - Try different lighting conditions
   - Use torch if available
   - Check if the barcode format is supported

4. **Build Errors**
   - Ensure Android SDK 35+ is installed
   - Check that all dependencies are properly configured
   - Verify Cordova and platform versions

### Debug Mode

Enable debug logging by checking the Android logcat:

```bash
adb logcat | grep BarcodeScanner
```

## License

MIT License - see LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## Support

For issues and questions:

1. Check the troubleshooting section
2. Search existing issues
3. Create a new issue with detailed information

## Changelog

### Version 1.0.0
- Initial release
- Data Matrix barcode scanning support
- CameraX integration
- ML Kit barcode detection
- Real-time camera preview
- Torch and zoom controls
- Permission handling 