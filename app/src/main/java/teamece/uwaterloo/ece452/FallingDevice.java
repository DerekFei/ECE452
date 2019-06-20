package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class FallingDevice implements GameObject {
    private boolean left;
    private boolean leftLane;
    private int width;
    private int height;
    private int altitude;
    private boolean start;
    private Rect hitBox;

    public FallingDevice(boolean left, boolean leftlane, int windowWidth, int windowHeight){
        this.left = left;
        this.leftLane = leftlane;
        width = windowWidth / 6;
        height = windowHeight / 8;
        altitude = - height / 2;
        start = false;
        hitBox = new Rect((int)((left ? 0 : width * 3) + (leftLane ? width * 0.25 : width * 1.75)), 0,
                (int)((left ? 0 : width * 3) + (leftLane ? width * 1.25 : width * 2.75)), 0);
    }

    public void update(){
        if(start) {
            altitude = altitude + 20;
            generateHitBox();
            if(altitude>9*height)
                start = false;
        }
    }

    public void initialize(){
        start = true;
        altitude = - height / 2;
        generateHitBox();
    }

    private void generateHitBox(){
        hitBox.set((int)((left ? 0 : width * 3) + (leftLane ? width * 0.25 : width * 1.75)), altitude - height / 2,
                (int)((left ? 0 : width * 3) + (leftLane ? width * 1.25 : width * 2.75)), altitude + height / 2);
    }

    public Rect getHitBox() {
        return this.hitBox;
    }

    public void draw(Canvas canvas, Bitmap bm){
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        canvas.drawBitmap(bm, (int)((left ? 0 : width * 3) + (leftLane ? width * 0.25 : width * 1.75)), altitude - height / 2, p);
    }

    public void terminate(){
        start = false;;
        altitude = - height / 2;
        generateHitBox();
    }

}
