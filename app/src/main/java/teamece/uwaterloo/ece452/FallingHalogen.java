package teamece.uwaterloo.ece452;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FallingHalogen extends FallingDevice implements GameObject {

    public FallingHalogen(Rect rect, int resID, Context context){
        super(rect, BitmapFactory.decodeResource(context.getResources(), resID));
    }
    @Override
    public void draw(Canvas canvas) {
//        super.draw(canvas, );
    }
}
