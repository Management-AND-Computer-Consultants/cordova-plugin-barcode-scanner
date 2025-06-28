package com.mccbarcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Barcode Scanner Plugin for Cordova
 * Supports Data Matrix and other barcode formats
 */
public class BarcodeScannerPlugin extends CordovaPlugin {
    
    private static final String TAG = "BarcodeScannerPlugin";
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private static final int SCAN_REQUEST = 1002;
    
    private CallbackContext currentCallbackContext;
    private boolean isInitialized = false;
    private BarcodeScannerActivity scannerActivity;
    
    // Plugin actions
    private static final String ACTION_INIT = "init";
    private static final String ACTION_SCAN = "scan";
    private static final String ACTION_DECODE = "decode";
    private static final String ACTION_START_SCANNING = "startScanning";
    private static final String ACTION_STOP_SCANNING = "stopScanning";
    private static final String ACTION_PAUSE_SCANNING = "pauseScanning";
    private static final String ACTION_RESUME_SCANNING = "resumeScanning";
    private static final String ACTION_SWITCH_TORCH = "switchTorch";
    private static final String ACTION_SET_ZOOM = "setZoom";
    private static final String ACTION_SET_FOCUS = "setFocus";
    private static final String ACTION_GET_RESOLUTION = "getResolution";
    private static final String ACTION_HAS_CAMERA = "hasCamera";
    private static final String ACTION_REQUEST_PERMISSIONS = "requestPermissions";
    private static final String ACTION_CHECK_PERMISSIONS = "checkPermissions";
    private static final String ACTION_DESTROY = "destroy";
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Executing action: " + action);
        
        switch (action) {
            case ACTION_INIT:
                return init(args, callbackContext);
            case ACTION_SCAN:
                return scan(args, callbackContext);
            case ACTION_DECODE:
                return decode(args, callbackContext);
            case ACTION_START_SCANNING:
                return startScanning(args, callbackContext);
            case ACTION_STOP_SCANNING:
                return stopScanning(callbackContext);
            case ACTION_PAUSE_SCANNING:
                return pauseScanning(callbackContext);
            case ACTION_RESUME_SCANNING:
                return resumeScanning(callbackContext);
            case ACTION_SWITCH_TORCH:
                return switchTorch(args, callbackContext);
            case ACTION_SET_ZOOM:
                return setZoom(args, callbackContext);
            case ACTION_SET_FOCUS:
                return setFocus(args, callbackContext);
            case ACTION_GET_RESOLUTION:
                return getResolution(callbackContext);
            case ACTION_HAS_CAMERA:
                return hasCamera(callbackContext);
            case ACTION_REQUEST_PERMISSIONS:
                return requestPermissions(callbackContext);
            case ACTION_CHECK_PERMISSIONS:
                return checkPermissions(callbackContext);
            case ACTION_DESTROY:
                return destroy(callbackContext);
            default:
                Log.e(TAG, "Unknown action: " + action);
                callbackContext.error("Unknown action: " + action);
                return false;
        }
    }
    
    private boolean init(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String license = args.getString(0);
        Log.d(TAG, "Initializing with license: " + license);
        
        // For now, we'll just mark as initialized
        // In a real implementation, you might validate the license
        isInitialized = true;
        
        callbackContext.success("Barcode scanner initialized successfully");
        return true;
    }
    
    private boolean scan(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (!isInitialized) {
            callbackContext.error("Scanner not initialized. Call init() first.");
            return false;
        }
        
        if (!checkCameraPermission()) {
            currentCallbackContext = callbackContext;
            requestCameraPermission();
            return true;
        }
        
        JSONObject options = args.getJSONObject(0);
        startScanActivity(options, callbackContext);
        return true;
    }
    
    private boolean decode(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String base64Data = args.getString(0);
        String format = args.getString(1);
        
        Log.d(TAG, "Decoding base64 data with format: " + format);
        
        // This would typically use a barcode decoding library
        // For now, we'll return an error indicating this needs implementation
        callbackContext.error("Decode from base64 not yet implemented");
        return false;
    }
    
    private boolean startScanning(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (!isInitialized) {
            callbackContext.error("Scanner not initialized. Call init() first.");
            return false;
        }
        
        if (!checkCameraPermission()) {
            currentCallbackContext = callbackContext;
            requestCameraPermission();
            return true;
        }
        
        JSONObject options = args.getJSONObject(0);
        startScanActivity(options, callbackContext);
        return true;
    }
    
    private boolean stopScanning(CallbackContext callbackContext) {
        if (scannerActivity != null) {
            scannerActivity.finish();
            scannerActivity = null;
        }
        callbackContext.success("Scanning stopped");
        return true;
    }
    
    private boolean pauseScanning(CallbackContext callbackContext) {
        if (scannerActivity != null) {
            scannerActivity.pauseScanning();
        }
        callbackContext.success("Scanning paused");
        return true;
    }
    
    private boolean resumeScanning(CallbackContext callbackContext) {
        if (scannerActivity != null) {
            scannerActivity.resumeScanning();
        }
        callbackContext.success("Scanning resumed");
        return true;
    }
    
    private boolean switchTorch(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String status = args.getString(0);
        if (scannerActivity != null) {
            scannerActivity.switchTorch(status.equals("on"));
        }
        callbackContext.success("Torch switched to: " + status);
        return true;
    }
    
    private boolean setZoom(JSONArray args, CallbackContext callbackContext) throws JSONException {
        double zoomFactor = args.getDouble(0);
        if (scannerActivity != null) {
            scannerActivity.setZoom(zoomFactor);
        }
        callbackContext.success("Zoom set to: " + zoomFactor);
        return true;
    }
    
    private boolean setFocus(JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject point = args.getJSONObject(0);
        float x = (float) point.getDouble("x");
        float y = (float) point.getDouble("y");
        
        if (scannerActivity != null) {
            scannerActivity.setFocus(x, y);
        }
        callbackContext.success("Focus set to: " + x + ", " + y);
        return true;
    }
    
    private boolean getResolution(CallbackContext callbackContext) {
        if (scannerActivity != null) {
            String resolution = scannerActivity.getResolution();
            callbackContext.success(resolution);
        } else {
            callbackContext.error("Scanner not active");
        }
        return true;
    }
    
    private boolean hasCamera(CallbackContext callbackContext) {
        boolean hasCamera = cordova.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        callbackContext.success(hasCamera);
        return true;
    }
    
    private boolean requestPermissions(CallbackContext callbackContext) {
        if (checkCameraPermission()) {
            callbackContext.success("Permissions already granted");
        } else {
            currentCallbackContext = callbackContext;
            requestCameraPermission();
        }
        return true;
    }
    
    private boolean checkPermissions(CallbackContext callbackContext) {
        boolean hasPermission = checkCameraPermission();
        callbackContext.success(hasPermission);
        return true;
    }
    
    private boolean destroy(CallbackContext callbackContext) {
        if (scannerActivity != null) {
            scannerActivity.finish();
            scannerActivity = null;
        }
        isInitialized = false;
        callbackContext.success("Scanner destroyed");
        return true;
    }
    
    private void startScanActivity(JSONObject options, CallbackContext callbackContext) {
        Intent intent = new Intent(cordova.getActivity(), BarcodeScannerActivity.class);
        intent.putExtra("options", options.toString());
        cordova.startActivityForResult(this, intent, SCAN_REQUEST);
        currentCallbackContext = callbackContext;
    }
    
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(cordova.getActivity(), Manifest.permission.CAMERA) 
               == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
            cordova.getActivity(),
            new String[]{Manifest.permission.CAMERA},
            CAMERA_PERMISSION_REQUEST
        );
    }
    
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (currentCallbackContext != null) {
                    currentCallbackContext.success("Camera permission granted");
                    currentCallbackContext = null;
                }
            } else {
                if (currentCallbackContext != null) {
                    currentCallbackContext.error("Camera permission denied");
                    currentCallbackContext = null;
                }
            }
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_REQUEST) {
            if (resultCode == cordova.getActivity().RESULT_OK && intent != null) {
                try {
                    String barcodeText = intent.getStringExtra("text");
                    String barcodeFormat = intent.getStringExtra("format");
                    
                    JSONObject result = new JSONObject();
                    result.put("text", barcodeText);
                    result.put("format", barcodeFormat);
                    result.put("success", true);
                    
                    if (currentCallbackContext != null) {
                        currentCallbackContext.success(result);
                        currentCallbackContext = null;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error creating result JSON", e);
                    if (currentCallbackContext != null) {
                        currentCallbackContext.error("Error processing scan result");
                        currentCallbackContext = null;
                    }
                }
            } else {
                if (currentCallbackContext != null) {
                    currentCallbackContext.error("Scan cancelled or failed");
                    currentCallbackContext = null;
                }
            }
        }
    }
} 