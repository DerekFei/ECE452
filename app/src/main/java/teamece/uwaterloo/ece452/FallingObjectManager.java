package teamece.uwaterloo.ece452;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;

public class FallingObjectManager {
    private int windowWidth;
    private int windowHeight;
    private int counter1, counter2, counter3, counter4;
    WeakReference<GameScene> gameScene;
    private FallingLED led1, led2, led3, led4;
    private FallingResistor r1, r2, r3, r4;
    private boolean rorl;

    public FallingObjectManager(int windowWidth, int windowHeight, GameScene gameScene, Resources r) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.gameScene = new WeakReference<>(gameScene);
        rorl = true;
        led1 = new FallingLED(true, true, windowWidth, windowHeight, r);
        led2 = new FallingLED(true, false, windowWidth, windowHeight, r);
        led3 = new FallingLED(false, true, windowWidth, windowHeight, r);
        led4 = new FallingLED(false, false, windowWidth, windowHeight, r);
        r1 = new FallingResistor(true, true, windowWidth, windowHeight, r);
        r2 = new FallingResistor(true, false, windowWidth, windowHeight, r);
        r3 = new FallingResistor(false, true, windowWidth, windowHeight, r);
        r4 = new FallingResistor(false, false, windowWidth, windowHeight, r);
        counter1 = ThreadLocalRandom.current().nextInt(0, 121);
        counter2 = ThreadLocalRandom.current().nextInt(0, 121);
        counter3 = ThreadLocalRandom.current().nextInt(0, 121);
        counter4 = ThreadLocalRandom.current().nextInt(0, 121);
        if (this.gameScene.get() != null) {
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(led1));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(led2));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(led3));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(led4));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(r1));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(r2));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(r3));
            this.gameScene.get().registerCollisionManager(new WeakReference<FallingDevice>(r4));
        }
    }

    public void update(){
        if(counter1 == 0) {
            if(rorl)
                r1.initialize();
            else
                led1.initialize();
            counter1 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter1 --;
        if(counter2 == 0) {
            if(rorl)
                r2.initialize();
            else
                led2.initialize();
            counter2 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter2 --;
        if(counter3 == 0) {
            if(rorl)
                r3.initialize();
            else
                led3.initialize();
            counter3 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter3 --;
        if(counter4 == 0) {
            if(rorl)
                r4.initialize();
            else
                led4.initialize();
            counter4 = ThreadLocalRandom.current().nextInt(120, 241);
            rorl = !rorl;
        }
        else
            counter4 --;
        led1.update();
        led2.update();
        led3.update();
        led4.update();
        r1.update();
        r2.update();
        r3.update();
        r4.update();
    }

    public void draw(Canvas canvas){
        led1.draw(canvas);
        led2.draw(canvas);
        led3.draw(canvas);
        led4.draw(canvas);
        r1.draw(canvas);
        r2.draw(canvas);
        r3.draw(canvas);
        r4.draw(canvas);
    }
}
