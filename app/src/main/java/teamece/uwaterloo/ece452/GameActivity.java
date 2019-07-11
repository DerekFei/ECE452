package teamece.uwaterloo.ece452;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.widget.Toast;

import java.io.IOException;

public class GameActivity extends AppCompatActivity {
    //ScreenRecording
    private static final int PERMISSION_CODE = 1;
    private int screenDensity;
    private int screenWidth;
    private int screenHeight;
    private boolean isRecording;
    private MediaRecorder mediaRecorder;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private GameScene gameScene;
    private MediaProjectionCallback callback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        gameScene = new GameScene(this);
        setContentView(gameScene);
        mediaRecorder = new MediaRecorder();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getWindowManager().getDefaultDisplay().getSize(size);
        screenDensity = displayMetrics.densityDpi;
        screenHeight = size.y;
        screenWidth = size.x;
        String[] Permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, Permissions, 0);
        recordScreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecordScreen();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PERMISSION_CODE) {
            Toast.makeText(this,
                    "Unknown permission code", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "User denied screen sharing permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "okok", Toast.LENGTH_SHORT).show();
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        callback = new MediaProjectionCallback();
        mediaProjection.registerCallback(callback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    private void recordScreen() {
        try {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GeECE.mp4");
            mediaRecorder.setVideoSize(screenWidth, screenHeight);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mediaRecorder.setVideoFrameRate(24);
            mediaRecorder.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        isRecording = true;
        if (gameScene.getHolder().getSurface() == null) {
            return;
        }
        if (mediaProjection == null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                    PERMISSION_CODE);
            return;
        }
    }
    private void stopRecordScreen() {
        isRecording = false;
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(callback);
            mediaProjection.stop();
            mediaProjection = null;
        }
    }
    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("ScreenRecording",
                screenWidth, screenHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
    }
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            mediaRecorder.stop();
            mediaRecorder.reset();
            stopRecordScreen();
        }
    }

//    @Override
//    public void onRequestPermissionResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//
//    }
}
