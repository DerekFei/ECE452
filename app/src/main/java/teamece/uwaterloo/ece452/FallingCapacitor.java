package teamece.uwaterloo.ece452;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FallingCapacitor extends FallingDevice implements GameObject {
    public FallingCapacitor(Rect rect, Bitmap bitmap){
            super(rect, bitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
