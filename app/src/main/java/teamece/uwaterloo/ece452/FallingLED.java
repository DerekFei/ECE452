package teamece.uwaterloo.ece452;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FallingLED extends FallingDevice implements GameObject {
    Bitmap b;

    public FallingLED(boolean left, boolean leftlane, int windowWidth, int windowHeight, Resources r){
        super(left, leftlane, windowWidth, windowHeight);
        b=BitmapFactory.decodeResource(r, R.drawable.led);
    }

    public void draw(Canvas canvas){
        super.draw(canvas, b);
    }
}
