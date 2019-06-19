package teamece.uwaterloo.ece452;

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

    public Goose(boolean l, int windowWidth, int windowHeight){
        left = l;
        leftLane = l;
        width = windowWidth / 6;
        height = windowHeight / 6;
        hitBox = new Rect((int)((left ? 0 : width * 3) + (leftLane ? width * 0.25 : width * 1.75)), height * 5,
                (int)((left ? 0 : width * 3) + (leftLane ? width * 1.25 : width * 2.75)), height * 6);
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawRect(hitBox, p);
    }

    public void update(){
        leftLane = !leftLane;
        generateHitBox();
    }

    public Rect getHitBox() {
        return this.hitBox;
    }

    private void generateHitBox(){
        hitBox.set((int)((left ? 0 : width * 3) + (leftLane ? width * 0.25 : width * 1.75)), height * 5,
                (int)((left ? 0 : width * 3) + (leftLane ? width * 1.25 : width * 2.75)), height * 6);
    }
}
