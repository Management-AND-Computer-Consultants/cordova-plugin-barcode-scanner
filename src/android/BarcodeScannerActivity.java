package com.managementandcomputerconsultants.barcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity for barcode scanning using CameraX and ML Kit
 */
public class BarcodeScannerActivity extends AppCompatActivity {
    
    private static final String TAG = "BarcodeScannerActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    
    private PreviewView previewView;
    private FrameLayout overlayView;
    private Button torchButton;
    private Button closeButton;
    
    private ExecutorService cameraExecutor;
    private Camera camera;
    private BarcodeScanner barcodeScanner;
    private boolean isScanning = true;
    private boolean torchEnabled = false;
    
    private String targetFormat = "DATA_MATRIX";
    private int resolution = 0; // AUTO
    private boolean torchOption = false;
    private boolean beepOnSuccess = false;
    private boolean vibrateOnSuccess = false;
    private float detectorSize = 0.6f;
    private boolean rotateCamera = false;
    
    /**
     * Convert barcodeFormats object to ML Kit format constants
     */
    private int getBarcodeFormatsFromObject(JSONObject barcodeFormats) {
        int formats = 0;
        
        try {
            // 1D Barcode Formats
            if (barcodeFormats.optBoolean("Code11", false)) {
                formats |= Barcode.FORMAT_CODE_11;
            }
            if (barcodeFormats.optBoolean("Code39", false)) {
                formats |= Barcode.FORMAT_CODE_39;
            }
            if (barcodeFormats.optBoolean("Code93", false)) {
                formats |= Barcode.FORMAT_CODE_93;
            }
            if (barcodeFormats.optBoolean("Code128", false)) {
                formats |= Barcode.FORMAT_CODE_128;
            }
            if (barcodeFormats.optBoolean("CodaBar", false)) {
                formats |= Barcode.FORMAT_CODABAR;
            }
            if (barcodeFormats.optBoolean("EAN8", false)) {
                formats |= Barcode.FORMAT_EAN_8;
            }
            if (barcodeFormats.optBoolean("EAN13", false)) {
                formats |= Barcode.FORMAT_EAN_13;
            }
            if (barcodeFormats.optBoolean("UPCA", false)) {
                formats |= Barcode.FORMAT_UPC_A;
            }
            if (barcodeFormats.optBoolean("UPCE", false)) {
                formats |= Barcode.FORMAT_UPC_E;
            }
            if (barcodeFormats.optBoolean("ITF", false)) {
                formats |= Barcode.FORMAT_ITF;
            }
            if (barcodeFormats.optBoolean("Industrial2Of5", false)) {
                formats |= Barcode.FORMAT_ITF;
            }
            if (barcodeFormats.optBoolean("ITF14", false)) {
                formats |= Barcode.FORMAT_ITF;
            }
            
            // 2D Barcode Formats
            if (barcodeFormats.optBoolean("QRCode", false)) {
                formats |= Barcode.FORMAT_QR_CODE;
            }
            if (barcodeFormats.optBoolean("DataMatrix", false)) {
                formats |= Barcode.FORMAT_DATA_MATRIX;
            }
            if (barcodeFormats.optBoolean("PDF417", false)) {
                formats |= Barcode.FORMAT_PDF417;
            }
            if (barcodeFormats.optBoolean("GS1DataBar", false)) {
                formats |= Barcode.FORMAT_GS1_DATABAR;
            }
            if (barcodeFormats.optBoolean("Maxicode", false)) {
                formats |= Barcode.FORMAT_MAXICODE;
            }
            if (barcodeFormats.optBoolean("MicroPDF417", false)) {
                formats |= Barcode.FORMAT_PDF417;
            }
            if (barcodeFormats.optBoolean("MicroQR", false)) {
                formats |= Barcode.FORMAT_QR_CODE;
            }
            if (barcodeFormats.optBoolean("PatchCode", false)) {
                formats |= Barcode.FORMAT_PATCH_CODE;
            }
            if (barcodeFormats.optBoolean("GS1Composite", false)) {
                formats |= Barcode.FORMAT_GS1_COMPOSITE;
            }
            if (barcodeFormats.optBoolean("PostalCode", false)) {
                formats |= Barcode.FORMAT_AZTEC;
            }
            if (barcodeFormats.optBoolean("DotCode", false)) {
                formats |= Barcode.FORMAT_AZTEC;
            }
            if (barcodeFormats.optBoolean("PharmaCode", false)) {
                formats |= Barcode.FORMAT_CODE_128;
            }
            if (barcodeFormats.optBoolean("Aztec", false)) {
                formats |= Barcode.FORMAT_AZTEC;
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing barcodeFormats", e);
        }
        
        // If no formats specified, default to all formats
        if (formats == 0) {
            formats = Barcode.FORMAT_ALL_FORMATS;
        }
        
        return formats;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        
        // Initialize views
        previewView = findViewById(R.id.preview_view);
        overlayView = findViewById(R.id.overlay_view);
        torchButton = findViewById(R.id.torch_button);
        closeButton = findViewById(R.id.close_button);
        
        // Get options from intent
        String optionsString = getIntent().getStringExtra("options");
        if (optionsString != null) {
            try {
                JSONObject options = new JSONObject(optionsString);
                
                // Parse barcodeFormats object
                if (options.has("barcodeFormats")) {
                    JSONObject barcodeFormats = options.getJSONObject("barcodeFormats");
                    targetFormat = barcodeFormats.toString(); // Store as string for later parsing
                } else {
                    // Fallback to old format
                    targetFormat = options.optString("format", "DATA_MATRIX");
                }
                
                // Parse resolution
                String resolutionStr = options.optString("resolution", "AUTO");
                switch (resolutionStr.toUpperCase()) {
                    case "480P":
                        resolution = 1;
                        break;
                    case "720P":
                        resolution = 2;
                        break;
                    case "1080P":
                        resolution = 3;
                        break;
                    case "2K":
                        resolution = 4;
                        break;
                    case "4K":
                        resolution = 5;
                        break;
                    default:
                        resolution = 0; // AUTO
                        break;
                }
                
                torchOption = options.optBoolean("torch", false);
                beepOnSuccess = options.optBoolean("beepOnSuccess", false);
                vibrateOnSuccess = options.optBoolean("vibrateOnSuccess", false);
                detectorSize = (float) options.optDouble("detectorSize", 0.6);
                rotateCamera = options.optBoolean("rotateCamera", false);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing options", e);
            }
        }
        
        // Initialize ML Kit barcode scanner
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(getBarcodeFormatsFromObject(new JSONObject(targetFormat)))
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);
        
        // Set up camera executor
        cameraExecutor = Executors.newSingleThreadExecutor();
        
        // Set up UI
        setupUI();
        
        // Request permissions and start camera
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
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
        // Create a custom view for the scanning overlay
        View overlay = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(4);
                
                // Draw scanning frame using detectorSize
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int frameSize = (int) (Math.min(getWidth(), getHeight()) * detectorSize);
                
                RectF frame = new RectF(
                    centerX - frameSize / 2,
                    centerY - frameSize / 2,
                    centerX + frameSize / 2,
                    centerY + frameSize / 2
                );
                
                canvas.drawRect(frame, paint);
                
                // Draw corner indicators
                paint.setStrokeWidth(8);
                int cornerLength = 50;
                
                // Top-left corner
                canvas.drawLine(frame.left, frame.top, frame.left + cornerLength, frame.top, paint);
                canvas.drawLine(frame.left, frame.top, frame.left, frame.top + cornerLength, paint);
                
                // Top-right corner
                canvas.drawLine(frame.right - cornerLength, frame.top, frame.right, frame.top, paint);
                canvas.drawLine(frame.right, frame.top, frame.right, frame.top + cornerLength, paint);
                
                // Bottom-left corner
                canvas.drawLine(frame.left, frame.bottom - cornerLength, frame.left, frame.bottom, paint);
                canvas.drawLine(frame.left, frame.bottom, frame.left + cornerLength, frame.bottom, paint);
                
                // Bottom-right corner
                canvas.drawLine(frame.right - cornerLength, frame.bottom, frame.right, frame.bottom, paint);
                canvas.drawLine(frame.right, frame.bottom - cornerLength, frame.right, frame.bottom, paint);
            }
        };
        
        overlayView.addView(overlay);
    }
    
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = 
            ProcessCameraProvider.getInstance(this);
        
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                
                // Set up preview use case
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                
                // Set up image analysis use case
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(getTargetResolution())
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                
                imageAnalysis.setAnalyzer(cameraExecutor, new BarcodeAnalyzer());
                
                // Select back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                
                // Unbind any bound use cases before rebinding
                cameraProvider.unbindAll();
                
                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }
    
    private Size getTargetResolution() {
        switch (resolution) {
            case 1: // 480P
                return new Size(640, 480);
            case 2: // 720P
                return new Size(1280, 720);
            case 3: // 1080P
                return new Size(1920, 1080);
            case 4: // 2K
                return new Size(2560, 1440);
            case 5: // 4K
                return new Size(3840, 2160);
            default: // AUTO
                return new Size(1280, 720);
        }
    }
    
    private class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            if (!isScanning) {
                imageProxy.close();
                return;
            }
            
            InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(), 
                imageProxy.getImageInfo().getRotationDegrees()
            );
            
            barcodeScanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            for (Barcode barcode : barcodes) {
                                String barcodeText = barcode.getRawValue();
                                if (barcodeText != null && !barcodeText.isEmpty()) {
                                    // Check if the detected format matches our target format
                                    if (isFormatMatch(barcode.getFormat())) {
                                        handleBarcodeDetected(barcodeText, barcode.getFormat());
                                        return;
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Barcode scanning failed", e);
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            imageProxy.close();
                        }
                    });
        }
    }
    
    /**
     * Check if the detected barcode format matches our target format
     */
    private boolean isFormatMatch(int detectedFormat) {
        try {
            JSONObject barcodeFormats = new JSONObject(targetFormat);
            int targetFormats = getBarcodeFormatsFromObject(barcodeFormats);
            return (detectedFormat & targetFormats) != 0;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing target format", e);
            return true; // Accept all formats if parsing fails
        }
    }
    
    /**
     * Play beep sound
     */
    private void playBeep() {
        if (beepOnSuccess) {
            try {
                android.media.ToneGenerator toneGen = new android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100);
                toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 150);
                toneGen.release();
            } catch (Exception e) {
                Log.e(TAG, "Error playing beep", e);
            }
        }
    }
    
    /**
     * Vibrate device
     */
    private void vibrate() {
        if (vibrateOnSuccess) {
            try {
                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(100);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error vibrating device", e);
            }
        }
    }
    
    /**
     * Convert ML Kit format constant to string format
     */
    private String getFormatString(int format) {
        switch (format) {
            case Barcode.FORMAT_CODE_11:
                return "CODE_11";
            case Barcode.FORMAT_CODE_39:
                return "CODE_39";
            case Barcode.FORMAT_CODE_93:
                return "CODE_93";
            case Barcode.FORMAT_CODE_128:
                return "CODE_128";
            case Barcode.FORMAT_CODABAR:
                return "CODABAR";
            case Barcode.FORMAT_EAN_8:
                return "EAN_8";
            case Barcode.FORMAT_EAN_13:
                return "EAN_13";
            case Barcode.FORMAT_UPC_A:
                return "UPC_A";
            case Barcode.FORMAT_UPC_E:
                return "UPC_E";
            case Barcode.FORMAT_ITF:
                return "ITF";
            case Barcode.FORMAT_QR_CODE:
                return "QR_CODE";
            case Barcode.FORMAT_DATA_MATRIX:
                return "DATA_MATRIX";
            case Barcode.FORMAT_PDF417:
                return "PDF417";
            case Barcode.FORMAT_GS1_DATABAR:
                return "GS1_DATABAR";
            case Barcode.FORMAT_MAXICODE:
                return "MAXICODE";
            case Barcode.FORMAT_PATCH_CODE:
                return "PATCH_CODE";
            case Barcode.FORMAT_GS1_COMPOSITE:
                return "GS1_COMPOSITE";
            case Barcode.FORMAT_AZTEC:
                return "AZTEC";
            default:
                return "UNKNOWN";
        }
    }
    
    private void handleBarcodeDetected(String barcodeText, int barcodeFormat) {
        isScanning = false;
        
        // Play beep and vibrate if enabled
        playBeep();
        vibrate();
        
        // Show toast message
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BarcodeScannerActivity.this, 
                    "Barcode detected: " + barcodeText + " (" + getFormatString(barcodeFormat) + ")", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Return result
        Intent resultIntent = new Intent();
        resultIntent.putExtra("barcodeText", barcodeText);
        resultIntent.putExtra("barcodeFormat", getFormatString(barcodeFormat));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    
    private void toggleTorch() {
        if (camera != null) {
            torchEnabled = !torchEnabled;
            camera.getCameraControl().enableTorch(torchEnabled);
            torchButton.setText(torchEnabled ? "Torch OFF" : "Torch ON");
        }
    }
    
    public void switchTorch(boolean enabled) {
        if (camera != null) {
            torchEnabled = enabled;
            camera.getCameraControl().enableTorch(enabled);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    torchButton.setText(torchEnabled ? "Torch OFF" : "Torch ON");
                }
            });
        }
    }
    
    public void setZoom(double zoomFactor) {
        if (camera != null) {
            camera.getCameraControl().setZoomRatio((float) zoomFactor);
        }
    }
    
    public void setFocus(float x, float y) {
        if (camera != null) {
            androidx.camera.core.MeteringPoint point = 
                new androidx.camera.core.MeteringPointFactory(previewView.getWidth(), previewView.getHeight())
                    .createPoint(x, y);
            androidx.camera.core.FocusMeteringAction action = 
                new androidx.camera.core.FocusMeteringAction.Builder(point).build();
            camera.getCameraControl().startFocusAndMetering(action);
        }
    }
    
    public String getResolution() {
        if (camera != null) {
            Size resolution = camera.getCameraInfo().getSensorRotationDegrees() % 180 == 0 
                ? camera.getCameraInfo().getSensorRect().size() 
                : new Size(camera.getCameraInfo().getSensorRect().height(), 
                          camera.getCameraInfo().getSensorRect().width());
            return resolution.getWidth() + "x" + resolution.getHeight();
        }
        return "Unknown";
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
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
} 