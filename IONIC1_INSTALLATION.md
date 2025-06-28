# Ionic 1/AngularJS Installation Guide

This guide will help you install and use the barcode scanner plugin in your Ionic 1/AngularJS project.

## Prerequisites

- Ionic 1.x project
- Cordova 6.0+ (but compatible with older versions)
- Android platform added to your project

## Installation Steps

### 1. Add the Plugin

```bash
# Navigate to your Ionic 1 project directory
cd your-ionic1-project

# Add the barcode scanner plugin
cordova plugin add @management-and-computer-consultants/cordova-plugin-barcode-scanner

# Or if you have the plugin locally
cordova plugin add /path/to/management-and-computer-consultants-cordova-plugin-barcode-scanner
```

### 2. Build the Project

```bash
# Build for Android
ionic build android

# Or using cordova directly
cordova build android
```

### 3. Test the Installation

```bash
# Run on device/emulator
ionic run android

# Or using cordova directly
cordova run android
```

## Usage in Ionic 1/AngularJS

### 1. Basic Usage in Controller

```javascript
angular.module('yourApp', [])
.controller('BarcodeController', function($scope) {
    
    // Initialize the scanner
    $scope.initScanner = function() {
        navigator.barcodeScanner.init('', function() {
            console.log('Scanner initialized successfully');
            $scope.$apply(function() {
                $scope.scannerReady = true;
            });
        }, function(error) {
            console.error('Scanner initialization failed:', error);
            $scope.$apply(function() {
                $scope.scannerError = error;
            });
        });
    };
    
    // Scan for barcodes
    $scope.scanBarcode = function() {
        var options = {
            barcodeFormats: {
                DataMatrix: true,
                QRCode: true,
                Code128: true,
                Code39: true
            },
            vibrateOnSuccess: true,
            torch: false,
            timeout: 30000
        };
        
        navigator.barcodeScanner.scan(options, function(result) {
            console.log('Barcode detected:', result);
            $scope.$apply(function() {
                $scope.scanResult = result;
                $scope.barcodeText = result.barcodeText;
                $scope.barcodeFormat = result.barcodeFormat;
            });
        }, function(error) {
            console.error('Scan failed:', error);
            $scope.$apply(function() {
                $scope.scanError = error;
            });
        });
    };
    
    // Check camera permissions
    $scope.checkPermissions = function() {
        navigator.barcodeScanner.checkPermissions(function(hasPermission) {
            $scope.$apply(function() {
                $scope.hasCameraPermission = hasPermission;
            });
        }, function(error) {
            console.error('Permission check failed:', error);
        });
    };
    
    // Request permissions
    $scope.requestPermissions = function() {
        navigator.barcodeScanner.requestPermissions(function() {
            console.log('Permissions granted');
            $scope.checkPermissions();
        }, function(error) {
            console.error('Permission request failed:', error);
        });
    };
    
    // Initialize when controller loads
    $scope.initScanner();
    $scope.checkPermissions();
});
```

### 2. HTML Template

```html
<ion-view view-title="Barcode Scanner">
    <ion-content>
        <div class="padding">
            <h2>Barcode Scanner</h2>
            
            <!-- Status -->
            <div class="card">
                <div class="item item-divider">
                    Scanner Status
                </div>
                <div class="item">
                    <p>Scanner Ready: {{scannerReady ? 'Yes' : 'No'}}</p>
                    <p>Camera Permission: {{hasCameraPermission ? 'Granted' : 'Denied'}}</p>
                </div>
            </div>
            
            <!-- Scan Button -->
            <button class="button button-positive button-block" 
                    ng-click="scanBarcode()" 
                    ng-disabled="!scannerReady">
                Scan Barcode
            </button>
            
            <!-- Permission Button -->
            <button class="button button-secondary button-block" 
                    ng-click="requestPermissions()" 
                    ng-if="!hasCameraPermission">
                Request Camera Permission
            </button>
            
            <!-- Results -->
            <div class="card" ng-if="scanResult">
                <div class="item item-divider">
                    Scan Result
                </div>
                <div class="item">
                    <p><strong>Text:</strong> {{barcodeText}}</p>
                    <p><strong>Format:</strong> {{barcodeFormat}}</p>
                </div>
            </div>
            
            <!-- Error Display -->
            <div class="card" ng-if="scanError">
                <div class="item item-divider item-assertive">
                    Error
                </div>
                <div class="item">
                    <p>{{scanError}}</p>
                </div>
            </div>
        </div>
    </ion-content>
</ion-view>
```

## Troubleshooting

### Common Issues

1. **Plugin not found**
   ```bash
   # Make sure you're in the correct directory
   cd your-ionic1-project
   
   # Remove and re-add the plugin
   cordova plugin remove management-and-computer-consultants-cordova-plugin-barcode-scanner
   cordova plugin add @management-and-computer-consultants/cordova-plugin-barcode-scanner
   ```

2. **Build errors**
   ```bash
   # Clean and rebuild
   cordova clean android
   cordova build android
   ```

3. **Permission issues**
   - Make sure to call `requestPermissions()` before scanning
   - Check that camera permission is granted in device settings

4. **Scanner not working**
   - Ensure `navigator.barcodeScanner` is available
   - Check console for error messages
   - Verify the plugin is properly installed with `cordova plugin list`

## API Reference

### Available Methods

- `navigator.barcodeScanner.init(license, success, error)`
- `navigator.barcodeScanner.scan(options, success, error)`
- `navigator.barcodeScanner.decode(base64Data, options, success, error)`
- `navigator.barcodeScanner.startScanning(options, onScanned, error)`
- `navigator.barcodeScanner.stopScanning(success, error)`
- `navigator.barcodeScanner.pauseScanning(success, error)`
- `navigator.barcodeScanner.resumeScanning(success, error)`
- `navigator.barcodeScanner.switchTorch(enabled, success, error)`
- `navigator.barcodeScanner.setZoom(zoomFactor, success, error)`
- `navigator.barcodeScanner.setFocus(point, success, error)`
- `navigator.barcodeScanner.getResolution(success, error)`
- `navigator.barcodeScanner.hasCamera(success, error)`
- `navigator.barcodeScanner.requestPermissions(success, error)`
- `navigator.barcodeScanner.checkPermissions(success, error)`
- `navigator.barcodeScanner.destroy(success, error)`

### Barcode Formats

- `DataMatrix` - Data Matrix barcodes
- `QRCode` - QR Codes
- `Code128` - Code 128
- `Code39` - Code 39
- `EAN13` - EAN-13
- `EAN8` - EAN-8
- `UPCA` - UPC-A
- `UPCE` - UPC-E
- `PDF417` - PDF417
- And many more...

## Support

For issues and questions:
1. Check the troubleshooting section above
2. Review the console logs for error messages
3. Ensure you're using the correct plugin ID: `cordova-plugin-barcode-scanner`
4. Verify your Ionic 1 and Cordova versions are compatible 