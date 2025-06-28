/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var exec = require('cordova/exec');

/**
 * Barcode Scanner Plugin
 * @namespace navigator.barcodeScanner
 */
var barcodeScanner = {};

/**
 * Barcode format constants
 * @readonly
 * @enum {string}
 */
barcodeScanner.BarcodeFormat = {
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
 * Camera resolution options
 * @readonly
 * @enum {string}
 */
barcodeScanner.Resolution = {
    AUTO: 'AUTO',
    RESOLUTION_480P: '480P',
    RESOLUTION_720P: '720P',
    RESOLUTION_1080P: '1080P',
    RESOLUTION_2K: '2K',
    RESOLUTION_4K: '4K'
};

/**
 * Default scanning options
 * @readonly
 * @type {Object}
 */
barcodeScanner.DefaultOptions = {
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
    vibrateOnSuccess: true,
    detectorSize: 0.6,
    rotateCamera: false,
    torch: false,
    resolution: 'AUTO',
    timeout: 30000
};

/**
 * Initialize the barcode scanner
 * @param {string} license - License key (optional for basic functionality)
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.init = function(license, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "init", [license || '']);
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
barcodeScanner.scan = function(options, successCallback, errorCallback) {
    // Merge with default options
    var scanOptions = Object.assign({}, barcodeScanner.DefaultOptions, options || {});
    
    // Ensure barcodeFormats is properly merged
    if (options && options.barcodeFormats) {
        scanOptions.barcodeFormats = Object.assign({}, barcodeScanner.DefaultOptions.barcodeFormats, options.barcodeFormats);
    }
    
    exec(successCallback, errorCallback, "BarcodeScanner", "scan", [scanOptions]);
};

/**
 * Decode barcode from base64 image data
 * @param {string} base64Data - Base64 encoded image data
 * @param {Object} options - Decoding options
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.decode = function(base64Data, options, successCallback, errorCallback) {
    var decodeOptions = Object.assign({}, barcodeScanner.DefaultOptions, options || {});
    exec(successCallback, errorCallback, "BarcodeScanner", "decode", [base64Data, decodeOptions]);
};

/**
 * Start continuous scanning
 * @param {Object} options - Scanning options
 * @param {Function} onScanned - Callback function called when barcode is detected
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.startScanning = function(options, onScanned, errorCallback) {
    var scanOptions = Object.assign({}, barcodeScanner.DefaultOptions, options || {});
    
    // Ensure barcodeFormats is properly merged
    if (options && options.barcodeFormats) {
        scanOptions.barcodeFormats = Object.assign({}, barcodeScanner.DefaultOptions.barcodeFormats, options.barcodeFormats);
    }
    
    exec(onScanned, errorCallback, "BarcodeScanner", "startScanning", [scanOptions]);
};

/**
 * Stop continuous scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.stopScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "stopScanning", []);
};

/**
 * Pause scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.pauseScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "pauseScanning", []);
};

/**
 * Resume scanning
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.resumeScanning = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "resumeScanning", []);
};

/**
 * Toggle torch/flashlight
 * @param {boolean} enabled - Enable or disable torch
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.switchTorch = function(enabled, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "switchTorch", [enabled]);
};

/**
 * Set camera zoom
 * @param {number} zoomFactor - Zoom factor (1.0 = normal, 2.0 = 2x zoom, etc.)
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.setZoom = function(zoomFactor, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "setZoom", [zoomFactor]);
};

/**
 * Set camera focus point
 * @param {Object} point - Focus point {x: number, y: number}
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.setFocus = function(point, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "setFocus", [point]);
};

/**
 * Get current camera resolution
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.getResolution = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "getResolution", []);
};

/**
 * Check if device has camera
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.hasCamera = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "hasCamera", []);
};

/**
 * Request camera permissions
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.requestPermissions = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "requestPermissions", []);
};

/**
 * Check camera permissions
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.checkPermissions = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "checkPermissions", []);
};

/**
 * Destroy the scanner instance
 * @param {Function} successCallback - Success callback function
 * @param {Function} errorCallback - Error callback function
 */
barcodeScanner.destroy = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "BarcodeScanner", "destroy", []);
};

module.exports = barcodeScanner; 