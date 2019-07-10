package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;

import com.firebase.ui.auth.data.model.Resource;

public abstract class FallingDevice implements GameObject {
    private Bitmap bitmap;
    private Rect hitBox;
    private boolean isValid;

    public FallingDevice(Rect hitBox, Bitmap bitmap){
        this.hitBox = hitBox;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, hitBox.width(), hitBox.height(), false); // Scales down the resource according to the Rect size
        this.isValid = true;
    }

    public void update(float inc){
        if (isValid) {
            hitBox.top += inc;
            hitBox.bottom += inc;
        }
    }

    public void update() {

    }

    public Rect getHitBox() {
        return this.hitBox;
    }

    public void draw(Canvas canvas){
        if (isValid) {
            Paint p = new Paint();
            canvas.drawBitmap(bitmap, hitBox.left, hitBox.top, p);
        }
    }

    public void invalidate() {
        isValid = false;
        hitBox.set(0, 4999, 0, 4999);
        bitmap = null;
    }

    public boolean equals(FallingDevice d)
    {
        return this.hitBox.equals(d.hitBox) &&
                ((this instanceof FallingLED && d instanceof FallingLED) ||
                (this instanceof FallingResistor && d instanceof FallingResistor) ||
                (this instanceof FallingCapacitor && d instanceof FallingCapacitor));
    }
}
