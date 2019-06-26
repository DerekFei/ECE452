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
    private int windowHeight;
    private int windowWidth;
    private int altitude;
    private boolean start;
    private Rect hitBox;

    public FallingDevice(boolean left, boolean leftlane, int windowWidth, int windowHeight){
        this.left = left;
        this.leftLane = leftlane;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        width = 250;
        height = 250;
        altitude = - height / 2;
        start = false;
        hitBox = new Rect(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), altitude - height / 2,
                ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), altitude + height / 2);
    }

    public void update(){
        if(start) {
            altitude = altitude + 20;
            generateHitBox();
            if(altitude>windowHeight+height)
                start = false;
        }
    }

    public void initialize(){
        start = true;
        altitude = - height / 2;
        generateHitBox();
    }

    private void generateHitBox(){
        hitBox.set(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), altitude - height / 2,
                ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), altitude + height / 2);
    }

    public Rect getHitBox() {
        return this.hitBox;
    }

    public void draw(Canvas canvas, Bitmap bm){
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawBitmap(bm, hitBox.left, hitBox.top, p);
    }

    public void terminate(){
        start = false;;
        altitude = - height / 2;
        generateHitBox();
    }

}
