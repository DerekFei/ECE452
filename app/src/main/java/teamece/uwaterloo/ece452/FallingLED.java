package teamece.uwaterloo.ece452;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.concurrent.ThreadLocalRandom;

public class FallingLED extends FallingDevice implements GameObject {
    public FallingLED(Rect rect, Bitmap bitmap){
        super(rect, bitmap);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
}
