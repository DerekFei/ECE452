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

    public void detect() {
        int size = this.devices.size();
        for (int i = 0; i < devices.size(); i++) {
            if (leftGoose.get() != null && rightGoose.get() != null && devices.get(i).get() != null) {
                if (Rect.intersects(devices.get(i).get().getHitBox(), leftGoose.get().getHitBox())) {
                    collide(devices.get(i), leftGoose);
                }
                if (Rect.intersects(devices.get(i).get().getHitBox(), rightGoose.get().getHitBox())) {
                    collide(devices.get(i), rightGoose);
                }
            }
        }
    }

    private void collide(WeakReference<FallingDevice> device, WeakReference<Goose> goose) {
        if (gameScene.get() != null) {
            gameScene.get().processCollision(device, goose);
        }
    }
}
