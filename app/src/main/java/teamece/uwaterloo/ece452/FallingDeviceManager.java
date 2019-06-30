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

    public FallingDeviceManager(int screenWidth, int screenHeight, GameScene gameScene, int initialSpawnInterval, Context context) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gameScene = new WeakReference<>(gameScene);
        this.startTime = this.initTime = System.currentTimeMillis();
        this.spawnInterval = initialSpawnInterval;
        this.devices = new ArrayList<>();
        this.context = context;

        int currY = -5 * screenHeight / 4;
        while (currY < 0) {
            spawnNewObject(50, currY, 300, currY + 250);
            currY += 250 + spawnInterval;
        }
    }

    //        new Rect(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), altitude - height / 2, ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), altitude + height / 2);

    public void update(){
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime)/4000.0)) * screenHeight / (10000.0f); // time in ms for an object to move down
        if (speed > 1) speed = 1;
        for (FallingDevice device : devices) {
            device.update(speed * elapsedTime);
        }
        if (devices.size() > 0) {
            if (devices.get(0).getHitBox().top >= screenHeight) {
                devices.remove(0);
                if (this.gameScene.get() != null) {
                    this.gameScene.get().unregisterFirstDeviceFromCollisionManager();
                }
                spawnNewObject();
            }
        }
    }

    private void spawnNewObject() {
        spawnNewObject(50, -250, 300, 0);
    }

    private void spawnNewObject(int l, int t, int r, int b) {
        FallingDevice device = new FallingLED(new Rect(l, t, r,  b), BitmapFactory.decodeResource(context.getResources(), R.drawable.led_red));
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
