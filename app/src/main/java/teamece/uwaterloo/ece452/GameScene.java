package teamece.uwaterloo.ece452;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class GameScene extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private Goose leftGoose;
    private Goose rightGoose;

    private int windowWidth;
    private int windowHeight;

    private FallingObjectManager mgr;
    private CollisionManager collisionManager;
    private WhiteLineManager whiteLineManager;

    private int score;

    public  GameScene (Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;
        score = 0;

        Resources r = getResources();
      
        leftGoose = new Goose(true, windowWidth, windowHeight, r);
        rightGoose = new Goose(false, windowWidth, windowHeight, r);
        collisionManager = new CollisionManager(leftGoose, rightGoose, this);
        mgr = new FallingObjectManager(windowWidth, windowHeight, this, r);
        whiteLineManager = new WhiteLineManager(windowWidth, windowHeight);

        setFocusable(true);
    }

    public void registerCollisionManager(WeakReference<FallingDevice> device) {
        if (device != null && collisionManager != null) {
            collisionManager.addObjectToWatch(device);
        }
    }

    public void processCollision(WeakReference<FallingDevice> device, WeakReference<Goose> goose) {
        if(device.get() instanceof FallingLED)
        {
            score += 1;
        }

        device.get().terminate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        boolean retry = true;
        try {
            thread.setRunning(false);
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        retry = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < windowWidth / 2)
                    leftGoose.update();
                else
                    rightGoose.update();
        }

        return true;
    }

    public void update() {
        mgr.update();
        collisionManager.detect();
        whiteLineManager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.DKGRAY);
        Paint yellowLinePaint = new Paint();
        yellowLinePaint.setColor(Color.YELLOW);
        Rect leftYellowLine = new Rect(windowWidth / 2 - 15, 0, windowWidth / 2 - 5, windowHeight);
        Rect rightYellowLine = new Rect(windowWidth / 2 + 5, 0, windowWidth / 2 + 15, windowHeight);
        canvas.drawRect(leftYellowLine, yellowLinePaint);
        canvas.drawRect(rightYellowLine, yellowLinePaint);

        whiteLineManager.draw(canvas);

        leftGoose.draw(canvas);
        rightGoose.draw(canvas);
        mgr.draw(canvas);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(100);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("" + (score), 30, windowHeight/20, textPaint);
    }
}
