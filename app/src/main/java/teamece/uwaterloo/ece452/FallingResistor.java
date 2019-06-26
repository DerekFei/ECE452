package teamece.uwaterloo.ece452;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class FallingResistor extends FallingDevice implements GameObject {
    Bitmap b;

    public FallingResistor(boolean left, boolean leftlane, int windowWidth, int windowHeight, Resources r){
        super(left, leftlane, windowWidth, windowHeight);
        b= BitmapFactory.decodeResource(r, R.drawable.resistor);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas, b);
    }
}
