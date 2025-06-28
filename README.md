# Cordova Barcode Scanner Plugin

A comprehensive barcode scanner plugin for Cordova supporting multiple 1D and 2D barcode formats, optimized for Data Matrix scanning.

## Features

- **Data Matrix Focus**: Optimized for Data Matrix barcode scanning
- **Multiple Formats**: Supports QR Code, Code 128, Code 39, EAN-13, UPC-A, PDF417, and more
- **Real-time Detection**: Live camera preview with instant barcode recognition
- **Camera Controls**: Torch/flashlight, zoom, and focus controls
- **Multiple Resolutions**: Support for various camera resolutions
- **Permission Handling**: Automatic camera permission requests
- **Cross-platform**: Android, iOS, and Browser support
- **Ionic 1 Compatible**: Works with Ionic 1/AngularJS projects

## Supported Barcode Formats

### 1D Barcodes
- Code 11
- Code 39
- Code 93
- Code 128
- Codabar
- EAN-8
- EAN-13
- UPC-A
- UPC-E
- Interleaved 2 of 5 (ITF)
- Industrial 2 of 5
- ITF-14

### 2D Barcodes
- **Data Matrix** (Primary focus)
- QR Code
- PDF417
- GS1 DataBar
- Maxicode
- Micro PDF417
- Micro QR
- Patch Code
- GS1 Composite
- Aztec

### Special Formats
- Postal Code
- Dot Code
- PharmaCode

## Installation

### Prerequisites

- Cordova 3.0.0 or higher
- Cordova Android 3.6.0 or higher
- Cordova iOS 3.6.0 or higher (for iOS support)

### Install the Plugin

```bash
# Add the plugin to your Cordova project
cordova plugin add @management-and-computer-consultants/cordova-plugin-barcode-scanner

# Or install from a local directory
cordova plugin add /path/to/management-and-computer-consultants-cordova-plugin-barcode-scanner
```

### For Ionic 1 Projects

See the detailed [Ionic 1 Installation Guide](IONIC1_INSTALLATION.md) for specific instructions.

## Usage

### Basic Scanning

```javascript
// Initialize the scanner
navigator.barcodeScanner.init('', function() {
    console.log('Scanner initialized');
}, function(error) {
    console.error('Init failed:', error);
});

// Scan for Data Matrix barcodes
navigator.barcodeScanner.scan({
    barcodeFormats: {
        DataMatrix: true,
        QRCode: true,
        Code128: true
    },
    vibrateOnSuccess: true,
    torch: false,
    timeout: 30000
}, function(result) {
    console.log('Barcode detected:', result.barcodeText);
    console.log('Format:', result.barcodeFormat);
}, function(error) {
    console.error('Scan failed:', error);
});
```

### Advanced Scanning Options

```javascript
// Custom scanning options
navigator.barcodeScanner.scan({
    barcodeFormats: {
        DataMatrix: true,
        QRCode: false,
        Code128: true,
        Code39: true
    },
    beepOnSuccess: false,
    vibrateOnSuccess: true,
    detectorSize: 0.7,
    rotateCamera: false,
    torch: true,
    resolution: '1080P',
    timeout: 60000
}, function(result) {
    // Handle successful scan
    alert('Detected: ' + result.barcodeText);
}, function(error) {
    // Handle error
    alert('Scan failed: ' + error);
});
```

### Continuous Scanning

```javascript
// Start continuous scanning
navigator.barcodeScanner.startScanning({
    barcodeFormats: {
        DataMatrix: true
    },
    vibrateOnSuccess: true
}, function(result) {
    // Called each time a barcode is detected
    console.log('Continuous scan result:', result);
}, function(error) {
    console.error('Continuous scan error:', error);
});

// Stop continuous scanning
navigator.barcodeScanner.stopScanning(function() {
    console.log('Scanning stopped');
}, function(error) {
    console.error('Error stopping scan:', error);
});
```

### Camera Controls

```javascript
// Toggle torch/flashlight
navigator.barcodeScanner.switchTorch(true, function() {
    console.log('Torch turned on');
}, function(error) {
    console.error('Torch error:', error);
});

// Set camera zoom
navigator.barcodeScanner.setZoom(2.0, function() {
    console.log('Zoom set to 2x');
}, function(error) {
    console.error('Zoom error:', error);
});

// Set focus point
navigator.barcodeScanner.setFocus({x: 0.5, y: 0.5}, function() {
    console.log('Focus set to center');
}, function(error) {
    console.error('Focus error:', error);
});
```

### Permission Handling

```javascript
// Check camera permissions
navigator.barcodeScanner.checkPermissions(function(hasPermission) {
    if (hasPermission) {
        console.log('Camera permission granted');
    } else {
        console.log('Camera permission denied');
    }
}, function(error) {
    console.error('Permission check failed:', error);
});

// Request camera permissions
navigator.barcodeScanner.requestPermissions(function() {
    console.log('Permissions granted');
}, function(error) {
    console.error('Permission request failed:', error);
});
```

### Utility Methods

```javascript
// Check if device has camera
navigator.barcodeScanner.hasCamera(function(hasCamera) {
    if (hasCamera) {
        console.log('Device has camera');
    } else {
        console.log('Device does not have camera');
    }
}, function(error) {
    console.error('Camera check failed:', error);
});

// Get current camera resolution
navigator.barcodeScanner.getResolution(function(resolution) {
    console.log('Current resolution:', resolution); // e.g., "1280x720"
}, function(error) {
    console.error('Resolution check failed:', error);
});

// Decode barcode from base64 image
navigator.barcodeScanner.decode(base64ImageData, {
    barcodeFormats: {DataMatrix: true}
}, function(result) {
    console.log('Decoded barcode:', result);
}, function(error) {
    console.error('Decode failed:', error);
});
```

## API Reference

### BarcodeScanner Object

#### Constants

- `navigator.barcodeScanner.BarcodeFormat.DATA_MATRIX` - Data Matrix format
- `navigator.barcodeScanner.BarcodeFormat.QR_CODE` - QR Code format
- `navigator.barcodeScanner.BarcodeFormat.CODE_128` - Code 128 format
- `navigator.barcodeScanner.BarcodeFormat.CODE_39` - Code 39 format
- `navigator.barcodeScanner.BarcodeFormat.EAN_13` - EAN-13 format
- `navigator.barcodeScanner.BarcodeFormat.EAN_8` - EAN-8 format
- `navigator.barcodeScanner.BarcodeFormat.UPC_A` - UPC-A format
- `navigator.barcodeScanner.BarcodeFormat.UPC_E` - UPC-E format

- `navigator.barcodeScanner.Resolution.AUTO` - Automatic resolution
- `navigator.barcodeScanner.Resolution.RESOLUTION_480P` - 480p resolution
- `navigator.barcodeScanner.Resolution.RESOLUTION_720P` - 720p resolution
- `navigator.barcodeScanner.Resolution.RESOLUTION_1080P` - 1080p resolution
- `navigator.barcodeScanner.Resolution.RESOLUTION_2K` - 2K resolution
- `navigator.barcodeScanner.Resolution.RESOLUTION_4K` - 4K resolution

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

## Platform Support

### Android
- Uses CameraX for modern camera handling
- Google ML Kit for barcode detection
- Supports all barcode formats
- Camera controls (torch, zoom, focus)
- Permission handling

### iOS
- Basic implementation provided
- Full implementation coming soon
- Uses AVFoundation framework

### Browser
- Mock implementation for development
- Returns appropriate error messages
- Useful for testing app logic

## Dependencies

### Android Dependencies
- androidx.core:core:1.3.0
- androidx.camera:camera-core:1.0.0
- androidx.camera:camera-camera2:1.0.0
- androidx.camera:camera-lifecycle:1.0.0
- androidx.camera:camera-view:1.0.0
- com.google.mlkit:barcode-scanning:16.1.0

### iOS Dependencies
- AVFoundation.framework
- CoreGraphics.framework

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
cordova plugin add ../management-and-computer-consultants-cordova-plugin-barcode-scanner

# Build and run
cordova build android
cordova run android
```

## Troubleshooting

### Common Issues

1. **"FORMAT_GS1_DATABAR not found" Error**
   - **Solution**: This error occurs when trying to use GS1 DataBar format which is not supported in Google Play Services Vision API. The plugin has been updated to remove this format from the supported formats list.

2. **"package R does not exist" Error**
   - **Solution**: This error occurs when the Android R class (containing resource references) is not generated properly. The plugin now uses programmatic layout creation to avoid this issue entirely.

3. **Camera Permission Denied**
   - **Solution**: Ensure your app requests camera permissions. The plugin handles this automatically on Android.

4. **Scanner Not Initializing**
   - **Solution**: Make sure you're calling `init()` before using other methods.

5. **No Barcode Detected**
   - **Solution**: Check that the barcode format is enabled in your options and that the barcode is clearly visible to the camera.

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
2. Review the [Ionic 1 Installation Guide](IONIC1_INSTALLATION.md) for Ionic 1 specific issues
3. Search existing issues
4. Create a new issue with detailed information

## Changelog

### Version 1.0.4
- Fixed plugin ID for Ionic 1 compatibility
- Lowered engine requirements for older Cordova versions
- Updated JavaScript interface to use `navigator.barcodeScanner`
- Added browser and iOS platform support
- Improved error handling and documentation

### Version 1.0.0
- Initial release
- Data Matrix barcode scanning support
- CameraX integration
- ML Kit barcode detection
- Real-time camera preview
- Torch and zoom controls
- Permission handling 

### "FORMAT_GS1_DATABAR not found" Error

**Solution**: This error occurs when trying to use GS1 DataBar format which is not supported in Google Play Services Vision API. The plugin has been updated to remove this format from the supported formats list.

**Additional Methods**:

```javascript
// Toggle torch/flashlight
navigator.barcodeScanner.switchTorch(enabled, successCallback, errorCallback)

// Check camera permissions
navigator.barcodeScanner.checkPermissions(successCallback, errorCallback)

// Request camera permissions
navigator.barcodeScanner.requestPermissions(successCallback, errorCallback)

// Check if device has camera
navigator.barcodeScanner.hasCamera(successCallback, errorCallback)
```

## Example

See the `example/index.html` file for a complete working example with a user interface for testing different scanning options.

## License

MIT License - see LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions, please use the GitHub issue tracker. 