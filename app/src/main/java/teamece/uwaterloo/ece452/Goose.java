package teamece.uwaterloo.ece452;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Goose implements GameObject {
    private Rect hitBox;
    private boolean left;
    private boolean leftLane;
    private int width;
    private int height;
    private int windowHeight;
    private int windowWidth;
    private Resources r;

    public Goose(boolean l, int windowWidth, int windowHeight, Resources r){
        left = l;
        leftLane = l;
        this.r=r;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        width = 250;
        height = 250;
        hitBox = new Rect(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), windowHeight - height,
                ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), windowHeight);
    }

    public void draw(Canvas canvas){
        Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.goose_eater);
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawBitmap(bm, hitBox.left, hitBox.top, p);
    }

    public void update(){
        leftLane = !leftLane;
        generateHitBox();
    }

    public Rect getHitBox() {
        return this.hitBox;
    }

    private void generateHitBox(){
        hitBox.set(((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) - width / 2), windowHeight - height,
                ((left ? windowWidth / 4 : windowWidth * 3 / 4) + (leftLane ? - windowWidth / 8 : windowWidth / 8) + width / 2), windowHeight);
    }
}
