package teamece.uwaterloo.ece452;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
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
    private ShareDialog shareDialog;
    private static final int REQUEST_PERMISSIONS = 1000;
    CallbackManager facebookCallbackMgr;

    private FacebookCallback<Sharer.Result> fbcallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.v("s", "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.v("s", "Sharing cancelled");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("s", "wtf cancelled");
            error.printStackTrace();
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        facebookCallbackMgr = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(facebookCallbackMgr, fbcallback);
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

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(findViewById(android.R.id.content), "permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(GameActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecordScreen();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackMgr.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PERMISSION_CODE) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        callback = new MediaProjectionCallback();
        mediaProjection.registerCallback(callback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    public void shareOnFb(){

        File externalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GeECE.mp4");
        Uri videoFileUri = Uri.fromFile(externalFile);

        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoFileUri)
                .build();

        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .setContentDescription("Feeling board? Join me and play Geece")
                .build();

        ShareDialog.show(this, content);
    }

    public void recordScreen() {
        isRecording = true;
        try {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GeECE.mp4");
            mediaRecorder.setVideoSize(screenWidth, screenHeight);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mediaRecorder.setVideoFrameRate(24);
            mediaRecorder.prepare();
            Log.i("aa", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GeECE.mp4");
            ///storage/emulated/0/DCIM/GeECE.mp4
            //file:///storage/emulated/0/DCIM/GeECE.mp4

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gameScene.getHolder().getSurface() == null) {
            return;
        }
        if (mediaProjection == null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                    PERMISSION_CODE);
            return;
        }
    }
    public void stopRecordScreen() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            isRecording = false;
        }

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
            stopRecordScreen();
        }
    }
}
