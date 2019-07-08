package teamece.uwaterloo.ece452;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FallingResistor extends FallingDevice implements GameObject {
    Bitmap b;

    public FallingResistor(Rect rect, int resID, Context context){
        super(rect, BitmapFactory.decodeResource(context.getResources(), resID));
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
}
