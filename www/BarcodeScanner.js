/**
 * Comprehensive Barcode Scanner Plugin for Cordova
 * Supports multiple 1D and 2D barcode formats with modern API
 */

var exec = require('cordova/exec');

/**
 * Barcode Scanner Plugin
 * @namespace cordova.plugins.mccbarcode.barcodeScanner
 */
var BarcodeScanner = {};

/**
 * Barcode format constants
 * @readonly
 * @enum {string}
 */
BarcodeScanner.BarcodeFormat = {
    // 1D Barcode Formats
    CODE_11: 'Code11',
    CODE_39: 'Code39',
    CODE_93: 'Code93',
    CODE_128: 'Code128',
    CODABAR: 'CodaBar',
    EAN_8: 'EAN8',
    EAN_13: 'EAN13',
    UPC_A: 'UPCA',
    UPC_E: 'UPCE',
    ITF: 'ITF',
    INDUSTRIAL_2_OF_5: 'Industrial2Of5',
    ITF_14: 'ITF14',
    
    // 2D Barcode Formats
    QR_CODE: 'QRCode',
    DATA_MATRIX: 'DataMatrix',
    PDF417: 'PDF417',
    GS1_DATABAR: 'GS1DataBar',
    MAXICODE: 'Maxicode',
    MICRO_PDF417: 'MicroPDF417',
    MICRO_QR: 'MicroQR',
    PATCH_CODE: 'PatchCode',
    GS1_COMPOSITE: 'GS1Composite',
    
    // Special Formats
    POSTAL_CODE: 'PostalCode',
    DOT_CODE: 'DotCode',
    PHARMA_CODE: 'PharmaCode',
    
    // Additional formats
    AZTEC: 'Aztec'
};

/**
 * Default scanning options
 * @readonly
 * @type {Object}
 */
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

/**
 * Scan for barcodes using the camera
 * @param {Object} options - Scanning options
 * @param {Object} [options.barcodeFormats] - Object specifying which barcode formats to scan for
 * @param {boolean} [options.beepOnSuccess] - Play beep sound on successful scan
 * @param {boolean} [options.vibrateOnSuccess] - Vibrate device on successful scan
 * @param {number} [options.detectorSize] - Size of detection area (0.0 to 1.0)
 * @param {boolean} [options.rotateCamera] - Rotate camera 180 degrees
 * @param {boolean} [options.torch] - Enable torch/flashlight
 * @param {string} [options.resolution] - Camera resolution
 * @param {number} [options.timeout] - Scan timeout in milliseconds
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.scan = function(options, successCallback, errorCallback) {
    // Merge with default options
    var scanOptions = Object.assign({}, BarcodeScanner.DefaultOptions, options || {});
    
    // Ensure barcodeFormats is properly merged
    if (options && options.barcodeFormats) {
        scanOptions.barcodeFormats = Object.assign({}, BarcodeScanner.DefaultOptions.barcodeFormats, options.barcodeFormats);
    }
    
    exec(successCallback, errorCallback, 'BarcodeScanner', 'scan', [scanOptions]);
};

/**
 * Initialize the barcode scanner
 * @param {string} license - License key (optional for basic functionality)
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.init = function(license, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'init', [license || '']);
};

/**
 * Decode barcode from base64 image data
 * @param {string} base64Data - Base64 encoded image data
 * @param {Object} options - Decoding options
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.decode = function(base64Data, options, successCallback, errorCallback) {
    var decodeOptions = Object.assign({}, BarcodeScanner.DefaultOptions, options || {});
    exec(successCallback, errorCallback, 'BarcodeScanner', 'decode', [base64Data, decodeOptions]);
};

/**
 * Start continuous scanning
 * @param {Object} options - Scanning options
 * @param {Function} onScanned - Callback function called when barcode is detected
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.startScanning = function(options, onScanned, errorCallback) {
    var scanOptions = Object.assign({}, BarcodeScanner.DefaultOptions, options || {});
    
    // Ensure barcodeFormats is properly merged
    if (options && options.barcodeFormats) {
        scanOptions.barcodeFormats = Object.assign({}, BarcodeScanner.DefaultOptions.barcodeFormats, options.barcodeFormats);
    }
    
    exec(onScanned, errorCallback, 'BarcodeScanner', 'startScanning', [scanOptions]);
};

/**
 * Stop continuous scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.stopScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'stopScanning', []);
};

/**
 * Pause scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.pauseScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'pauseScanning', []);
};

/**
 * Resume scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.resumeScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'resumeScanning', []);
};

/**
 * Toggle torch/flashlight
 * @param {boolean} enabled - Enable or disable torch
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.switchTorch = function(enabled, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'switchTorch', [enabled]);
};

/**
 * Set camera zoom
 * @param {number} zoomFactor - Zoom factor (1.0 = normal, 2.0 = 2x zoom, etc.)
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.setZoom = function(zoomFactor, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'setZoom', [zoomFactor]);
};

/**
 * Set camera focus point
 * @param {Object} point - Focus point {x: number, y: number}
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.setFocus = function(point, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'setFocus', [point]);
};

/**
 * Get current camera resolution
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.getResolution = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'getResolution', []);
};

/**
 * Check if device has camera
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.hasCamera = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'hasCamera', []);
};

/**
 * Request camera permissions
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.requestPermissions = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'requestPermissions', []);
};

/**
 * Check camera permissions
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.checkPermissions = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'checkPermissions', []);
};

/**
 * Destroy scanner instance
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
BarcodeScanner.destroy = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'BarcodeScanner', 'destroy', []);
};

// Export to cordova.plugins.mccbarcode.barcodeScanner
if (typeof cordova !== 'undefined' && cordova.plugins) {
    if (!cordova.plugins.mccbarcode) {
        cordova.plugins.mccbarcode = {};
    }
    cordova.plugins.mccbarcode.barcodeScanner = BarcodeScanner;
}

// Also export as BarcodeScanner for backward compatibility
module.exports = BarcodeScanner; 