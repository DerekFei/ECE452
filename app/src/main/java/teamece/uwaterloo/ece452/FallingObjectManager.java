package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import java.util.concurrent.ThreadLocalRandom;

public class FallingObjectManager {
    private int windowWidth;
    private int windowHeight;
    private int counter1, counter2, counter3, counter4;

    private FallingLED led1, led2, led3, led4;

    public FallingObjectManager(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        led1 = new FallingLED(true, true, windowWidth, windowHeight);
        led2 = new FallingLED(true, false, windowWidth, windowHeight);
        led3 = new FallingLED(false, true, windowWidth, windowHeight);
        led4 = new FallingLED(false, false, windowWidth, windowHeight);
        counter1 = ThreadLocalRandom.current().nextInt(0, 121);
        counter2 = ThreadLocalRandom.current().nextInt(0, 121);
        counter3 = ThreadLocalRandom.current().nextInt(0, 121);
        counter4 = ThreadLocalRandom.current().nextInt(0, 121);
    }

    public void update(){
        if(counter1 == 0) {
            led1.initialize();
            counter1 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter1 --;
        if(counter2 == 0) {
            led2.initialize();
            counter2 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter2 --;
        if(counter3 == 0) {
            led3.initialize();
            counter3 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter3 --;
        if(counter4 == 0) {
            led4.initialize();
            counter4 = ThreadLocalRandom.current().nextInt(120, 241);
        }
        else
            counter4 --;
        led1.update();
        led2.update();
        led3.update();
        led4.update();
    }

    public void draw(Canvas canvas){
        led1.draw(canvas);
        led2.draw(canvas);
        led3.draw(canvas);
        led4.draw(canvas);
    }
}