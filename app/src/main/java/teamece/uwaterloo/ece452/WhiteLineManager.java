package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WhiteLineManager {
    int w,h;
    Rect LL[] = new Rect[4];
    Rect RL[] = new Rect[4];

    public WhiteLineManager(int windowWidth, int windowHeight) {
        w = windowWidth;
        h = windowHeight;
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
        for(int i=0; i<4; i++)
        {
            LL[i].top = LL[i].top + 20;
            LL[i].bottom = LL[i].bottom + 20;
            if(LL[i].top >= h)
            {
                LL[i].top = - h / 3;
                LL[i].bottom = - h * 2 / 9;
            }
        }

        for(int i=0; i<4; i++)
        {
            RL[i].top = RL[i].top + 20;
            RL[i].bottom = RL[i].bottom + 20;
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
        }
        for(int i=0; i<4; i++)
        {
            canvas.drawRect(RL[i], barPaint);
        }
    }
}
