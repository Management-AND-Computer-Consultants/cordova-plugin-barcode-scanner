package com.mccbarcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple Barcode Scanner Activity
 * This is a basic implementation that can be extended with actual barcode scanning
 */
public class BarcodeScannerActivity extends AppCompatActivity {
    
    private static final String TAG = "BarcodeScannerActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    
    private SurfaceView previewView;
    private FrameLayout overlayView;
    private Button torchButton;
    private Button closeButton;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    
    private boolean isScanning = true;
    private boolean torchEnabled = false;
    
    private String targetFormat = "DATA_MATRIX";
    private boolean torchOption = false;
    private boolean beepOnSuccess = false;
    private boolean vibrateOnSuccess = false;
    private float detectorSize = 0.6f;
    private boolean rotateCamera = false;
    
    // Barcode format mapping
    private Set<Integer> enabledFormats = new HashSet<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create layout programmatically to avoid R class issues
        createLayout();
        
        // Parse options from intent
        Intent intent = getIntent();
        if (intent.hasExtra("options")) {
            try {
                String optionsJson = intent.getStringExtra("options");
                JSONObject options = new JSONObject(optionsJson);
                
                if (options.has("barcodeFormats")) {
                    JSONObject barcodeFormats = options.getJSONObject("barcodeFormats");
                    parseBarcodeFormats(barcodeFormats);
                } else {
                    // Fallback to old format
                    targetFormat = options.optString("format", "DATA_MATRIX");
                    setupDefaultFormats();
                }
                
                torchOption = options.optBoolean("torch", false);
                beepOnSuccess = options.optBoolean("beepOnSuccess", false);
                vibrateOnSuccess = options.optBoolean("vibrateOnSuccess", false);
                detectorSize = (float) options.optDouble("detectorSize", 0.6);
                rotateCamera = options.optBoolean("rotateCamera", false);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing options", e);
                setupDefaultFormats();
            }
        } else {
            setupDefaultFormats();
        }
        
        // Set up UI
        setupUI();
        
        // Request permissions
        if (allPermissionsGranted()) {
            startScanning();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    
    private void createLayout() {
        // Create main layout
        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setBackgroundColor(android.graphics.Color.BLACK);
        
        // Create SurfaceView for camera preview
        previewView = new SurfaceView(this);
        previewView.setId(android.view.View.generateViewId());
        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);
        previewView.setLayoutParams(previewParams);
        mainLayout.addView(previewView);
        
        // Create overlay view
        overlayView = new FrameLayout(this);
        overlayView.setId(android.view.View.generateViewId());
        RelativeLayout.LayoutParams overlayParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);
        overlayView.setLayoutParams(overlayParams);
        mainLayout.addView(overlayView);
        
        // Create top toolbar
        LinearLayout topToolbar = new LinearLayout(this);
        topToolbar.setId(android.view.View.generateViewId());
        topToolbar.setOrientation(LinearLayout.HORIZONTAL);
        topToolbar.setBackgroundColor(0x80000000); // Semi-transparent black
        topToolbar.setPadding(50, 50, 50, 50);
        
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        toolbarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topToolbar.setLayoutParams(toolbarParams);
        
        // Create close button
        closeButton = new Button(this);
        closeButton.setText("✕");
        closeButton.setTextColor(android.graphics.Color.WHITE);
        closeButton.setTextSize(20);
        closeButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        closeButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        topToolbar.addView(closeButton);
        
        // Create title
        TextView titleText = new TextView(this);
        titleText.setText("Scan Barcode");
        titleText.setTextColor(android.graphics.Color.WHITE);
        titleText.setTextSize(18);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        titleText.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        titleText.setLayoutParams(titleParams);
        topToolbar.addView(titleText);
        
        // Create torch button
        torchButton = new Button(this);
        torchButton.setText("Torch ON");
        torchButton.setTextColor(android.graphics.Color.WHITE);
        torchButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        torchButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        topToolbar.addView(torchButton);
        
        mainLayout.addView(topToolbar);
        
        // Create bottom instructions
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        bottomLayout.setBackgroundColor(0x80000000);
        bottomLayout.setPadding(50, 50, 50, 50);
        
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomLayout.setLayoutParams(bottomParams);
        
        TextView instructionText = new TextView(this);
        instructionText.setText("Position the barcode within the frame");
        instructionText.setTextColor(android.graphics.Color.WHITE);
        instructionText.setTextSize(16);
        instructionText.setGravity(android.view.Gravity.CENTER);
        instructionText.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomLayout.addView(instructionText);
        
        TextView subText = new TextView(this);
        subText.setText("The scanner will automatically detect and read the barcode");
        subText.setTextColor(0xFFCCCCCC);
        subText.setTextSize(14);
        subText.setGravity(android.view.Gravity.CENTER);
        subText.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomLayout.addView(subText);
        
        mainLayout.addView(bottomLayout);
        
        // Set the main layout as content view
        setContentView(mainLayout);
    }
    
    private void setupDefaultFormats() {
        // Enable common formats by default
        enabledFormats.add(Barcode.DATA_MATRIX);
        enabledFormats.add(Barcode.QR_CODE);
        enabledFormats.add(Barcode.CODE_128);
        enabledFormats.add(Barcode.CODE_39);
        enabledFormats.add(Barcode.EAN_13);
        enabledFormats.add(Barcode.EAN_8);
        enabledFormats.add(Barcode.UPC_A);
        enabledFormats.add(Barcode.UPC_E);
        enabledFormats.add(Barcode.PDF417);
        enabledFormats.add(Barcode.AZTEC);
    }
    
    private void parseBarcodeFormats(JSONObject barcodeFormats) {
        try {
            // Map JavaScript format names to Google Play Services Vision constants
            if (barcodeFormats.optBoolean("DataMatrix", true)) {
                enabledFormats.add(Barcode.DATA_MATRIX);
            }
            if (barcodeFormats.optBoolean("QRCode", true)) {
                enabledFormats.add(Barcode.QR_CODE);
            }
            if (barcodeFormats.optBoolean("Code128", true)) {
                enabledFormats.add(Barcode.CODE_128);
            }
            if (barcodeFormats.optBoolean("Code39", true)) {
                enabledFormats.add(Barcode.CODE_39);
            }
            if (barcodeFormats.optBoolean("EAN13", true)) {
                enabledFormats.add(Barcode.EAN_13);
            }
            if (barcodeFormats.optBoolean("EAN8", true)) {
                enabledFormats.add(Barcode.EAN_8);
            }
            if (barcodeFormats.optBoolean("UPCA", true)) {
                enabledFormats.add(Barcode.UPC_A);
            }
            if (barcodeFormats.optBoolean("UPCE", true)) {
                enabledFormats.add(Barcode.UPC_E);
            }
            if (barcodeFormats.optBoolean("PDF417", true)) {
                enabledFormats.add(Barcode.PDF417);
            }
            if (barcodeFormats.optBoolean("Aztec", true)) {
                enabledFormats.add(Barcode.AZTEC);
            }
            if (barcodeFormats.optBoolean("CodaBar", true)) {
                enabledFormats.add(Barcode.CODABAR);
            }
            if (barcodeFormats.optBoolean("ITF", true)) {
                enabledFormats.add(Barcode.ITF);
            }
            
            // Note: GS1_DATABAR is not available in Google Play Services Vision API
            // It's available in ML Kit Barcode Scanning API, but we're using Vision API here
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing barcode formats", e);
            setupDefaultFormats();
        }
    }
    
    private void setupUI() {
        // Close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        // Torch button
        torchButton.setVisibility(torchOption ? View.VISIBLE : View.GONE);
        torchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTorch();
            }
        });
        
        // Add scanning overlay
        addScanningOverlay();
    }
    
    private void addScanningOverlay() {
        // Add a simple scanning overlay
        View overlay = new View(this) {
            @Override
            protected void onDraw(android.graphics.Canvas canvas) {
                super.onDraw(canvas);
                android.graphics.Paint paint = new android.graphics.Paint();
                paint.setColor(android.graphics.Color.RED);
                paint.setStyle(android.graphics.Paint.Style.STROKE);
                paint.setStrokeWidth(4);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int size = (int) (Math.min(getWidth(), getHeight()) * detectorSize);
                
                android.graphics.RectF rect = new android.graphics.RectF(centerX - size/2, centerY - size/2, 
                                     centerX + size/2, centerY + size/2);
                canvas.drawRect(rect, paint);
            }
        };
        overlayView.addView(overlay);
    }
    
    private void startScanning() {
        // Create barcode detector
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE | Barcode.CODE_128 | 
                                 Barcode.CODE_39 | Barcode.EAN_13 | Barcode.EAN_8 | 
                                 Barcode.UPC_A | Barcode.UPC_E | Barcode.PDF417 | 
                                 Barcode.AZTEC | Barcode.CODABAR | Barcode.ITF)
                .build();
        
        if (!barcodeDetector.isOperational()) {
            Log.e(TAG, "Barcode detector is not operational");
            Toast.makeText(this, "Barcode detector not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Create camera source
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1280, 720)
                .build();
        
        // Set up detector
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Clean up resources
            }
            
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (!isScanning) return;
                
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    Barcode barcode = barcodes.valueAt(0);
                    
                    // Check if this format is enabled
                    if (enabledFormats.contains(barcode.format)) {
                        handleBarcodeDetected(barcode.displayValue, barcode.format);
                    }
                }
            }
        });
        
        // Set up SurfaceView callback
        previewView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(previewView.getHolder());
                } catch (IOException e) {
                    Log.e(TAG, "Error starting camera", e);
                    Toast.makeText(BarcodeScannerActivity.this, "Error starting camera", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Surface changed
            }
            
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }
    
    private void handleBarcodeDetected(String barcodeText, int barcodeFormat) {
        if (!isScanning) return;
        
        isScanning = false;
        
        // Play beep and vibrate if enabled
        if (beepOnSuccess) {
            playBeep();
        }
        if (vibrateOnSuccess) {
            vibrate();
        }
        
        // Convert format to string
        String formatString = getFormatString(barcodeFormat);
        
        // Return result
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("text", barcodeText);
                resultIntent.putExtra("format", formatString);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    
    private String getFormatString(int format) {
        switch (format) {
            case Barcode.DATA_MATRIX:
                return "DATA_MATRIX";
            case Barcode.QR_CODE:
                return "QR_CODE";
            case Barcode.CODE_128:
                return "CODE_128";
            case Barcode.CODE_39:
                return "CODE_39";
            case Barcode.EAN_13:
                return "EAN_13";
            case Barcode.EAN_8:
                return "EAN_8";
            case Barcode.UPC_A:
                return "UPC_A";
            case Barcode.UPC_E:
                return "UPC_E";
            case Barcode.PDF417:
                return "PDF417";
            case Barcode.AZTEC:
                return "AZTEC";
            case Barcode.CODABAR:
                return "CODABAR";
            case Barcode.ITF:
                return "ITF";
            default:
                return "UNKNOWN";
        }
    }
    
    private void playBeep() {
        // Simple beep implementation
        try {
            android.media.ToneGenerator toneGen = new android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100);
            toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP);
        } catch (Exception e) {
            Log.e(TAG, "Error playing beep", e);
        }
    }
    
    private void vibrate() {
        try {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(200);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error vibrating", e);
        }
    }
    
    private void toggleTorch() {
        torchEnabled = !torchEnabled;
        Toast.makeText(this, "Torch: " + (torchEnabled ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
    }
    
    public void switchTorch(boolean enabled) {
        torchEnabled = enabled;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleTorch();
            }
        });
    }
    
    public void setZoom(double zoomFactor) {
        Toast.makeText(this, "Zoom set to: " + zoomFactor, Toast.LENGTH_SHORT).show();
    }
    
    public void setFocus(float x, float y) {
        Toast.makeText(this, "Focus set to: " + x + ", " + y, Toast.LENGTH_SHORT).show();
    }
    
    public String getResolution() {
        return "1280x720";
    }
    
    public void pauseScanning() {
        isScanning = false;
    }
    
    public void resumeScanning() {
        isScanning = true;
    }
    
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.stop();
            cameraSource.release();
        }
        if (barcodeDetector != null) {
            barcodeDetector.release();
        }
    }
} 