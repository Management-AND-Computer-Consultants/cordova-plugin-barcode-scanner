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
 * Browser implementation of Barcode Scanner
 * This provides a mock implementation for browser platform
 */
var BarcodeScannerProxy = {};

/**
 * Mock barcode scanner for browser platform
 */
BarcodeScannerProxy.init = function(license, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - init called');
    if (successCallback) {
        successCallback('Browser platform - scanner initialized');
    }
};

BarcodeScannerProxy.scan = function(options, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - scan called');
    if (errorCallback) {
        errorCallback('Barcode scanning not supported in browser platform');
    }
};

BarcodeScannerProxy.decode = function(base64Data, options, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - decode called');
    if (errorCallback) {
        errorCallback('Barcode decoding not supported in browser platform');
    }
};

BarcodeScannerProxy.startScanning = function(options, onScanned, errorCallback) {
    console.log('BarcodeScanner: Browser platform - startScanning called');
    if (errorCallback) {
        errorCallback('Continuous scanning not supported in browser platform');
    }
};

BarcodeScannerProxy.stopScanning = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - stopScanning called');
    if (successCallback) {
        successCallback('Browser platform - scanning stopped');
    }
};

BarcodeScannerProxy.pauseScanning = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - pauseScanning called');
    if (successCallback) {
        successCallback('Browser platform - scanning paused');
    }
};

BarcodeScannerProxy.resumeScanning = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - resumeScanning called');
    if (successCallback) {
        successCallback('Browser platform - scanning resumed');
    }
};

BarcodeScannerProxy.switchTorch = function(enabled, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - switchTorch called');
    if (errorCallback) {
        errorCallback('Torch not supported in browser platform');
    }
};

BarcodeScannerProxy.setZoom = function(zoomFactor, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - setZoom called');
    if (errorCallback) {
        errorCallback('Camera zoom not supported in browser platform');
    }
};

BarcodeScannerProxy.setFocus = function(point, successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - setFocus called');
    if (errorCallback) {
        errorCallback('Camera focus not supported in browser platform');
    }
};

BarcodeScannerProxy.getResolution = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - getResolution called');
    if (successCallback) {
        successCallback('Browser platform - resolution not available');
    }
};

BarcodeScannerProxy.hasCamera = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - hasCamera called');
    if (successCallback) {
        successCallback(false);
    }
};

BarcodeScannerProxy.requestPermissions = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - requestPermissions called');
    if (errorCallback) {
        errorCallback('Camera permissions not supported in browser platform');
    }
};

BarcodeScannerProxy.checkPermissions = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - checkPermissions called');
    if (successCallback) {
        successCallback(false);
    }
};

BarcodeScannerProxy.destroy = function(successCallback, errorCallback) {
    console.log('BarcodeScanner: Browser platform - destroy called');
    if (successCallback) {
        successCallback('Browser platform - scanner destroyed');
    }
};

module.exports = BarcodeScannerProxy; 