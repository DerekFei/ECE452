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

        setFocusable(true);
    }

    public void registerCollisionManager(WeakReference<FallingDevice> device) {
        if (device != null && collisionManager != null) {
            System.out.println("added");
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
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        canvas.drawLine((float)windowWidth / 4, (float)0, (float)windowWidth / 4, (float)windowHeight, linePaint);
        canvas.drawLine((float)windowWidth / 2, (float)0, (float)windowWidth / 2, (float)windowHeight, linePaint);
        canvas.drawLine((float)windowWidth * 3 / 4, (float)0, (float)windowWidth * 3 / 4, (float)windowHeight, linePaint);

        leftGoose.draw(canvas);
        rightGoose.draw(canvas);
        mgr.draw(canvas);

        Rect topBar = new Rect(0, 0, windowWidth, windowHeight/10);
        Paint barPaint = new Paint();
        barPaint.setColor(Color.WHITE);
        canvas.drawRect(topBar, barPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Score: " + Integer.toString(score), windowWidth/2, windowHeight/20, textPaint);
    }
}
