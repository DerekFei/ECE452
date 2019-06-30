package teamece.uwaterloo.ece452;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.ThreadLocalRandom;

public class FallingLED extends FallingDevice implements GameObject {
    Bitmap b;

    public FallingLED(boolean left, boolean leftLane, int windowWidth, int windowHeight, Resources r){
        super(left, leftLane, windowWidth, windowHeight);
        int counter = ThreadLocalRandom.current().nextInt(1, 4);
        int color;
        System.out.println("" + counter);
        switch (counter) {
            case 1:
                color = R.drawable.led_green;
                break;
            case 2:
                color = R.drawable.led_red;
                break;
            case 3:
                color = R.drawable.led_yellow;
                break;
            default:
                color = R.drawable.led_red;
                break;
        }
        b=BitmapFactory.decodeResource(r, color);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas, b);
    }
}
