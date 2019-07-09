package teamece.uwaterloo.ece452;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.health.SystemHealthManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FallingDeviceManager {
    private int screenWidth;
    private int screenHeight;
    private long startTime;
    private long initTime;
    private int spawnInterval;
    private ArrayList<FallingDevice> devices;
    private Context context;
    WeakReference<GameScene> gameScene;
    private int countdown = 0;
    private int leftOffset = 0;
    private int rightOffset = 0;
    private boolean leftLeftLane;
    private boolean rightLeftLane;

    public FallingDeviceManager(int screenWidth, int screenHeight, GameScene gameScene, int initialSpawnInterval, Context context) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gameScene = new WeakReference<>(gameScene);
        this.startTime = this.initTime = System.currentTimeMillis();
        this.spawnInterval = initialSpawnInterval;
        this.devices = new ArrayList<>();
        this.context = context;
    }

    // new Rect(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), altitude - height / 2, ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), altitude + height / 2);

    public void update(){
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime)/4000.0)) * screenHeight / (10000.0f); // time in ms for an object to move down
        if (speed > 1) speed = 1;
        for (FallingDevice device : devices) {
            device.update(speed * elapsedTime);
            if (device.getHitBox().top >= screenHeight) {
                devices.remove(device);
                this.gameScene.get().unregisterExpiredDevices();
            }
        }
        countdown -=elapsedTime;
        leftOffset -= elapsedTime;
        rightOffset -= elapsedTime;
        if(countdown <= 0)
        {
            countdown = spawnInterval;
            leftLeftLane = ((int) Math.floor(Math.random()*2) == 0);
            rightLeftLane = ((int) Math.floor(Math.random()*2) == 0);
            leftOffset = (int)Math.floor(Math.random()*4000);
            rightOffset = (int)Math.floor(Math.random()*4000);
        }
        if(leftOffset <= 0)
        {
            leftOffset = 999999999;
            if(leftLeftLane)
            {
                spawnNewObject(screenWidth / 40, - screenWidth / 5, screenWidth * 9 / 40, 0);
            }
            else
            {
                spawnNewObject(screenWidth * 11 / 40, - screenWidth / 5, screenWidth * 19 / 40, 0);
            }
        }
        if(rightOffset <= 0)
        {
            rightOffset = 999999999;
            if(rightLeftLane)
            {
                spawnNewObject(screenWidth * 21 / 40, - screenWidth / 5, screenWidth * 29 / 40, 0);
            }
            else
            {
                spawnNewObject(screenWidth * 31 / 40, - screenWidth / 5, screenWidth * 39 / 40, 0);
            }
        }
    }

    private void spawnNewObject(int l, int t, int r, int b) {
        FallingDevice device = new FallingResistor(new Rect(l, t, r, b), BitmapFactory.decodeResource(context.getResources(), R.drawable.resistor));
        devices.add(device);
        if (this.gameScene.get() != null) {
            this.gameScene.get().registerCollisionManager(new WeakReference<>(device));
        }
    }

    public void draw(Canvas canvas){
        for (FallingDevice device : devices) {
            device.draw(canvas);
        }
    }
}
