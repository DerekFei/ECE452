package teamece.uwaterloo.ece452;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FallingInductor extends FallingDevice implements GameObject {
    public FallingInductor(Rect rect, Bitmap bitmap){
        super(rect, bitmap);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
}
