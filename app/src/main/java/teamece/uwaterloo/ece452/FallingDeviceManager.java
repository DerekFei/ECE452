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

    private int ledSpawnInterval;
    private int ledCounter;

    private int capacitorSpawnInterval;
    private int capacitorCounter;

    public FallingDeviceManager(int screenWidth, int screenHeight, GameScene gameScene, int initialSpawnInterval, Context context) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gameScene = new WeakReference<>(gameScene);
        this.startTime = this.initTime = System.currentTimeMillis();
        this.spawnInterval = initialSpawnInterval;
        this.devices = new ArrayList<>();
        this.context = context;
        this.ledSpawnInterval = 3000;
        this.ledCounter = ledSpawnInterval+(int)(Math.random()*ledSpawnInterval/4 - ledSpawnInterval/8);
        this.capacitorSpawnInterval = 10000;
        this.capacitorCounter = capacitorSpawnInterval+(int)(Math.random()*capacitorSpawnInterval/4 - capacitorSpawnInterval/8);
    }

    public void update(){
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime)/4000.0)) * screenHeight / (10000.0f); // time in ms for an object to move down
        if (speed > 1) speed = 1;
        for (int i=0; i<devices.size(); i++) {
            FallingDevice device = devices.get(i);
            device.update(speed * elapsedTime);
            if (device.getHitBox().top >= screenHeight*1.1) {
                devices.remove(device);
                i--;
                this.gameScene.get().unregisterExpiredDevices();
            }
        }

        //Block for resistor spawning
        countdown -=elapsedTime;
        leftOffset -= elapsedTime;
        rightOffset -= elapsedTime;
        if(countdown <= 0)
        {
            countdown = (int)(spawnInterval / speed);
            leftLeftLane = ((int) Math.floor(Math.random()*2) == 0);
            rightLeftLane = ((int) Math.floor(Math.random()*2) == 0);
            leftOffset = (int)Math.floor(Math.random()*countdown/3);
            rightOffset = (int)Math.floor(Math.random()*countdown/3);
        }
        if(leftOffset <= 0)
        {
            leftOffset = 999999999;
            if(leftLeftLane)
            {
                spawnNewResistor(screenWidth / 40, - screenWidth / 5, screenWidth * 9 / 40, 0);
            }
            else
            {
                spawnNewResistor(screenWidth * 11 / 40, - screenWidth / 5, screenWidth * 19 / 40, 0);
            }
        }
        if(rightOffset <= 0)
        {
            rightOffset = 999999999;
            if(rightLeftLane)
            {
                spawnNewResistor(screenWidth * 21 / 40, - screenWidth / 5, screenWidth * 29 / 40, 0);
            }
            else
            {
                spawnNewResistor(screenWidth * 31 / 40, - screenWidth / 5, screenWidth * 39 / 40, 0);
            }
        }

        //Block for LED spawning
        ledCounter -= elapsedTime;
        if(ledCounter <= 0)
        {
            ledCounter = (int)(ledSpawnInterval / speed)+(int)(Math.random()*ledSpawnInterval/4 - ledSpawnInterval/8);
            boolean left = Math.floor(Math.random()*2) == 0;
            boolean leftLane = Math.floor(Math.random()*2) == 0;
            spawnNewLED(((left ? screenWidth / 4 : screenWidth * 3 / 4) + (leftLane ? - screenWidth / 8 : screenWidth / 8) - screenWidth / 10), - screenWidth / 5,
                    ((left ? screenWidth / 4 : screenWidth * 3 / 4) + (leftLane ? - screenWidth / 8 : screenWidth / 8) + screenWidth / 10), 0);
        }

        //Block for capacitor spawning
        capacitorCounter -= elapsedTime;
        if(capacitorCounter <= 0)
        {
            capacitorCounter = (int)(capacitorSpawnInterval / speed)+(int)(Math.random()*capacitorSpawnInterval/4 - capacitorSpawnInterval/8);
            boolean left = Math.floor(Math.random()*2) == 0;
            boolean leftLane = Math.floor(Math.random()*2) == 0;
            spawnNewCapacitor(((left ? screenWidth / 4 : screenWidth * 3 / 4) + (leftLane ? - screenWidth / 8 : screenWidth / 8) - screenWidth / 10), - screenWidth / 5,
                    ((left ? screenWidth / 4 : screenWidth * 3 / 4) + (leftLane ? - screenWidth / 8 : screenWidth / 8) + screenWidth / 10), 0);
        }
    }

    private void spawnNewCapacitor(int l, int t, int r, int b) {
        int color = (int) Math.floor(Math.random()*3);
        FallingDevice device = new FallingCapacitor(new Rect(l, t, r, b), BitmapFactory.decodeResource(context.getResources(), R.drawable.capacitor));
        devices.add(device);
        if (this.gameScene.get() != null) {
            this.gameScene.get().registerCollisionManager(new WeakReference<>(device));
        }
    }

    private void spawnNewLED(int l, int t, int r, int b) {
        int color = (int) Math.floor(Math.random()*3);
        FallingDevice device = new FallingLED(new Rect(l, t, r, b), BitmapFactory.decodeResource(context.getResources(),
                color == 0 ? R.drawable.led_green : (color == 1 ? R.drawable.led_red : R.drawable.led_yellow)));
        devices.add(device);
        if (this.gameScene.get() != null) {
            this.gameScene.get().registerCollisionManager(new WeakReference<>(device));
        }
    }

    private void spawnNewResistor(int l, int t, int r, int b) {
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

    public void unpause() {
        startTime = System.currentTimeMillis();
    }
}
