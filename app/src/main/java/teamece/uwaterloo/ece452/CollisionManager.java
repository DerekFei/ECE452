package teamece.uwaterloo.ece452;

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
        devices.add(device);
    }

    public void detect() {
        for (int i = 0; i < devices.size(); i++) {
            if (leftGoose.get() != null && rightGoose.get() != null && devices.get(i).get() != null) {
                if (devices.get(i).get().getHitBox().intersect(leftGoose.get().getHitBox())) {
                    devices.get(i).get().initialize();
                    System.out.println("colliding with left");
                }
                if (devices.get(i).get().getHitBox().intersect(leftGoose.get().getHitBox())) {
                    devices.get(i).get().initialize();
                    System.out.println("colliding with right");
                }
            }
        }
    }
}
