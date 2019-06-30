package teamece.uwaterloo.ece452;

import android.graphics.Rect;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CollisionManager {

    private WeakReference<Goose> leftGoose;
    private WeakReference<Goose> rightGoose;
    private WeakReference<GameScene> gameScene;
    private ArrayList<WeakReference<FallingDevice>> devices;
    public CollisionManager(Goose leftGoose, Goose rightGoose, GameScene gameScene) {
        this.leftGoose = new WeakReference<>(leftGoose);
        this.rightGoose = new WeakReference<>(rightGoose);
        this.gameScene = new WeakReference<>(gameScene);
        this.devices = new ArrayList<>();
    }

    public void addObjectToWatch(WeakReference<FallingDevice> device) {
        this.devices.add(device);
    }
    public void removeFirstObject() { this.devices.remove(0); }
    public void detect() {
        if (leftGoose.get() == null || rightGoose.get() == null) return;
        for (WeakReference<FallingDevice> device : devices) {
            if (device.get() == null) continue;
            if (Rect.intersects(device.get().getHitBox(), leftGoose.get().getHitBox())) {
                collide(device, leftGoose);
            }
            if (Rect.intersects(device.get().getHitBox(), rightGoose.get().getHitBox())) {
                collide(device, rightGoose);
            }
        }
    }

    private void collide(WeakReference<FallingDevice> device, WeakReference<Goose> goose) {
        if (gameScene.get() != null) {
            gameScene.get().processCollision(device, goose);
        }
    }
}
