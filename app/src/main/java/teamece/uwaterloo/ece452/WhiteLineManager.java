package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WhiteLineManager {
    int w,h;
    private long startTime;
    private long initTime;
    Rect LL[] = new Rect[4];
    Rect RL[] = new Rect[4];

    public WhiteLineManager(int windowWidth, int windowHeight) {
        w = windowWidth;
        h = windowHeight;
        this.startTime = this.initTime = System.currentTimeMillis();
        for(int i=0; i<4; i++)
        {
            Rect R = new Rect(windowWidth / 4 - 5, - windowHeight / 3 + i * windowHeight / 3, windowWidth / 4 + 5, - windowHeight * 2 / 9 + i * windowHeight / 3);
            LL[i] = R;
        }
        for(int i=0; i<4; i++)
        {
            Rect R = new Rect(windowWidth * 3 / 4 - 5, - windowHeight / 3 + i * windowHeight / 3, windowWidth * 3 / 4 + 5, - windowHeight * 2 / 9 + i * windowHeight / 3);
            RL[i] = R;
        }
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime)/4000.0)) * h / (10000.0f); // time in ms for an object to move down
        if (speed > 1) speed = 1;
        float modifier = speed * elapsedTime;
        for(int i=0; i<4; i++)
        {
            LL[i].top += modifier;
            LL[i].bottom += modifier;
            RL[i].top += modifier;
            RL[i].bottom += modifier;
            if(LL[i].top >= h)
            {
                LL[i].top = - h / 3;
                LL[i].bottom = - h * 2 / 9;
            }
            if(RL[i].top >= h)
            {
                RL[i].top = - h / 3;
                RL[i].bottom = - h * 2 / 9;
            }
        }
    }

    public void draw(Canvas canvas){
        Paint barPaint = new Paint();
        barPaint.setColor(Color.WHITE);
        for(int i=0; i<4; i++)
        {
            canvas.drawRect(LL[i], barPaint);
            canvas.drawRect(RL[i], barPaint);
        }
    }
}
