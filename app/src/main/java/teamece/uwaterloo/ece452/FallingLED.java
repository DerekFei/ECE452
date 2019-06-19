package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FallingLED extends FallingDevice implements GameObject {
    public FallingLED(boolean left, boolean leftlane, int windowWidth, int windowHeight){
        super(left, leftlane, windowWidth, windowHeight);
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        super.draw(canvas, p);
    }
}
