package teamece.uwaterloo.ece452;

import android.graphics.Rect;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CollisionManager {

    private WeakReference<Goose> leftGoose;
    private WeakReference<Goose> rightGoose;
    private WeakReference<GameScene> gameScene;
    private ArrayList<WeakReference<FallingDevice>> devices;
    private int screenHeight;
    public CollisionManager(Goose leftGoose, Goose rightGoose, GameScene gameScene, int screenHeight) {
        this.leftGoose = new WeakReference<>(leftGoose);
        this.rightGoose = new WeakReference<>(rightGoose);
        this.gameScene = new WeakReference<>(gameScene);
        this.devices = new ArrayList<>();
        this.screenHeight = screenHeight;
    }

    public void addObjectToWatch(WeakReference<FallingDevice> device) {
        this.devices.add(device);
    }

    public void removeExpiredDevices() {
        for (WeakReference<FallingDevice> device : devices) {
            if (device.get() == null) continue;
            if (device.get().getHitBox().top >= screenHeight) {
                devices.remove(device);
            }
        }
    }

    public void detect() {
        if (leftGoose.get() == null || rightGoose.get() == null) return;
        for (WeakReference<FallingDevice> device : devices) {
            if (device.get() == null) continue;
            if (Rect.intersects(device.get().getHitBox(), leftGoose.get().getHitBox()) || Rect.intersects(device.get().getHitBox(), rightGoose.get().getHitBox())) {
                collide(device);
            }
        }
    }

    private void collide(WeakReference<FallingDevice> device) {
        if (gameScene.get() != null) {
            gameScene.get().processCollision(device);
        }
    }
}
