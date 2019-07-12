package teamece.uwaterloo.ece452;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.view.WindowManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.ref.WeakReference;

public class GameScene extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private Goose leftGoose;
    private Goose rightGoose;

    private int windowWidth;
    private int windowHeight;

    private FallingDeviceManager mgr;
    private CollisionManager collisionManager;
    private WhiteLineManager whiteLineManager;

    private int score;
    private int life;
    private boolean dead;
    private boolean paused;
    private boolean recording;
    private Resources r;

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
        r = getResources();
        score = 0;
        life = 10;
        dead = false;
        paused = false;
      
        leftGoose = new Goose(true, windowWidth, windowHeight, r);
        rightGoose = new Goose(false, windowWidth, windowHeight, r);
        collisionManager = new CollisionManager(leftGoose, rightGoose, this, windowHeight);
        mgr = new FallingDeviceManager(windowWidth, windowHeight, this, 8000, context);
        whiteLineManager = new WhiteLineManager(windowWidth, windowHeight);
        setFocusable(true);
    }

    public void registerCollisionManager(WeakReference<FallingDevice> device) {
        if (device != null && collisionManager != null) {
            collisionManager.addObjectToWatch(device);
        }
    }
    public void unregisterExpiredDevices() {
        if (collisionManager != null) {
            collisionManager.removeExpiredDevices();
        }
    }
    public void processCollision(WeakReference<FallingDevice> device) {
        if (device.get() instanceof FallingLED)
        {
            score += 1;
        }
        else if (device.get() instanceof FallingResistor)
        {
            if (life > 0) life -= 1;
        }
        if (device.get() != null) device.get().invalidate();
        if(life==0)
        {
            dead = true;
            recording = false;
            ((GameActivity) getContext()).stopRecordScreen();
            checkUpload();
        }
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
        try {
            thread.setRunning(false);
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(paused && !dead)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (Math.pow((event.getX() - windowWidth / 2), 2) + Math.pow((event.getY() - windowHeight / 10), 2) < Math.pow(windowWidth / 8, 2)) {
                        paused = false;
                    }
                    else if(Math.pow((event.getX() - windowWidth / 4), 2) + Math.pow((event.getY() - windowHeight*4/5), 2) < Math.pow(windowWidth / 12, 2))
                    {
                        if(recording) {
                            recording = false;
                            ((GameActivity) getContext()).stopRecordScreen();
                            //Stop recording. The video should be ready at this point
                        }
                        else
                        {
                            recording = true;
                            //Start recording.
                            ((GameActivity) getContext()).recordScreen();
                        }
                    }
                    else if(Math.pow((event.getX() - windowWidth * 3 / 4), 2) + Math.pow((event.getY() - windowHeight*4/5), 2) < Math.pow(windowWidth / 12, 2))
                    {
                        Intent homeActivity = new Intent(getContext(), HomeScreenActivity.class);
                        getContext().startActivity(homeActivity);
                    }
            }
        }
        else if(!paused && !dead)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (Math.pow((event.getX() - windowWidth / 2), 2) + Math.pow((event.getY() - windowHeight / 10), 2) < Math.pow(windowWidth / 8, 2)) {
                        paused = true;
                    }
                    else if (event.getX() < windowWidth / 2)
                        leftGoose.update();
                    else
                        rightGoose.update();
            }
        }
        else if(dead)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(Math.pow((event.getX()-windowWidth/2),2)+Math.pow((event.getY()-windowHeight*3/5),2)<Math.pow(windowWidth/6,2))
                    {
                        score = 0;
                        life = 10;
                        dead = false;

                        leftGoose = new Goose(true, windowWidth, windowHeight, r);
                        rightGoose = new Goose(false, windowWidth, windowHeight, r);
                        collisionManager = new CollisionManager(leftGoose, rightGoose, this, windowHeight);
                        mgr = new FallingDeviceManager(windowWidth, windowHeight, this,700, getContext());
                        whiteLineManager = new WhiteLineManager(windowWidth, windowHeight);
                    }
                    else if(Math.pow((event.getX()-windowWidth/4),2)+Math.pow((event.getY()-windowHeight*4/5),2)<Math.pow(windowWidth/9,2))
                    {
                        Intent homeActivity = new Intent(getContext(), HomeScreenActivity.class);
                        getContext().startActivity(homeActivity);
                    }
                    else if(Math.pow((event.getX()-windowWidth/2),2)+Math.pow((event.getY()-windowHeight*4/5),2)<Math.pow(windowWidth/9,2))
                    {
                        Intent leaderActivity = new Intent(getContext(), LeaderBoardActivity.class);
                        getContext().startActivity(leaderActivity);
                    }
                    else if(Math.pow((event.getX()-windowWidth*3/4),2)+Math.pow((event.getY()-windowHeight*4/5),2)<Math.pow(windowWidth/9,2))
                    {
                        //Share to Facebook
                    }
            }
        }
        else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
            }
        }

        return true;
    }

    public void update() {
        if(!dead && !paused) {
            mgr.update();
            collisionManager.detect();
            whiteLineManager.update();
        }
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

        Paint lifePaint = new Paint();
        lifePaint.setColor(Color.WHITE);
        lifePaint.setStrokeWidth(5);
        lifePaint.setTextSize(windowWidth/15);
        lifePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("" + (life), 0, windowHeight/20, lifePaint);

        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setStrokeWidth(5);
        scorePaint.setTextSize(windowWidth/15);
        scorePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("" + (score), windowWidth, windowHeight/20, scorePaint);

        Paint pausePaint = new Paint();
        pausePaint.setAlpha(100);
        if(paused && !dead)
        {
            Paint tintPaint = new Paint();
            tintPaint.setColor(Color.BLACK);
            tintPaint.setAlpha(200);
            canvas.drawRect(0,0,windowWidth,windowHeight,tintPaint);

            Bitmap resumeBm = BitmapFactory.decodeResource(r, R.drawable.resume);
            resumeBm = Bitmap.createScaledBitmap(resumeBm, windowWidth / 4, windowWidth / 4, false);
            canvas.drawBitmap(resumeBm, windowWidth * 3 / 8, windowHeight / 10 - windowWidth / 8, pausePaint);

            Paint pauseTextPaint = new Paint();
            pauseTextPaint.setColor(Color.WHITE);
            pauseTextPaint.setStrokeWidth(5);
            pauseTextPaint.setTextSize(windowWidth/12);
            pauseTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME PAUSED", windowWidth/2, windowHeight*2/5, pauseTextPaint);

            Paint circlePaint = new Paint();
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(Color.WHITE);
            canvas.drawCircle(windowWidth/4, windowHeight*4/5, windowWidth/6, circlePaint);
            canvas.drawCircle(windowWidth*3/4, windowHeight*4/5, windowWidth/6, circlePaint);

            Paint bitMapPaint = new Paint();
            bitMapPaint.setColor(Color.WHITE);
            if(recording)
            {
                Bitmap stopBm = BitmapFactory.decodeResource(r, R.drawable.stop_record);
                stopBm = Bitmap.createScaledBitmap(stopBm, windowWidth/6, windowWidth/6, false);
                canvas.drawBitmap(stopBm, windowWidth / 6, windowHeight * 4 / 5 - windowWidth / 12, bitMapPaint);
            }
            else {
                Bitmap recordBm = BitmapFactory.decodeResource(r, R.drawable.record);
                recordBm = Bitmap.createScaledBitmap(recordBm, windowWidth / 6, windowWidth / 6, false);
                canvas.drawBitmap(recordBm, windowWidth / 6, windowHeight * 4 / 5 - windowWidth / 12, bitMapPaint);
            }
            Bitmap homeBm = BitmapFactory.decodeResource(r, R.drawable.gameover_home);
            homeBm = Bitmap.createScaledBitmap(homeBm, windowWidth/6, windowWidth/6, false);
            canvas.drawBitmap(homeBm, windowWidth*2/3, windowHeight*4/5-windowWidth/12, bitMapPaint);
        }
        else if (!paused && !dead){
            Bitmap pauseBm = BitmapFactory.decodeResource(r, R.drawable.pause);
            pauseBm = Bitmap.createScaledBitmap(pauseBm, windowWidth / 4, windowWidth / 4, false);
            canvas.drawBitmap(pauseBm, windowWidth * 3 / 8, windowHeight / 10 - windowWidth / 8, pausePaint);
        }
        else
        {
            Paint tintPaint = new Paint();
            tintPaint.setColor(Color.BLACK);
            tintPaint.setAlpha(200);
            canvas.drawRect(0,0,windowWidth,windowHeight,tintPaint);

            Paint gameoverPaint = new Paint();
            gameoverPaint.setColor(Color.WHITE);
            gameoverPaint.setStrokeWidth(5);
            gameoverPaint.setTextSize(windowWidth/12);
            scorePaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER", windowWidth/4, windowHeight/5, gameoverPaint);
            canvas.drawText("Score: " + score, windowWidth/3, windowHeight*2/5, gameoverPaint);

            Paint circlePaint = new Paint();
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(Color.WHITE);
            canvas.drawCircle(windowWidth/2, windowHeight*3/5, windowWidth/6, circlePaint);
            canvas.drawCircle(windowWidth/4, windowHeight*4/5, windowWidth/9, circlePaint);
            canvas.drawCircle(windowWidth/2, windowHeight*4/5, windowWidth/9, circlePaint);
            canvas.drawCircle(windowWidth*3/4, windowHeight*4/5, windowWidth/9, circlePaint);

            Paint bitMapPaint = new Paint();
            bitMapPaint.setColor(Color.WHITE);
            Bitmap replayBm = BitmapFactory.decodeResource(r, R.drawable.gameover_replay);
            replayBm = Bitmap.createScaledBitmap(replayBm, windowWidth/6, windowWidth/6, false);
            canvas.drawBitmap(replayBm, windowWidth*5/12, windowHeight*3/5-windowWidth/12, bitMapPaint);
            Bitmap homeBm = BitmapFactory.decodeResource(r, R.drawable.gameover_home);
            homeBm = Bitmap.createScaledBitmap(homeBm, windowWidth/9, windowWidth/9, false);
            canvas.drawBitmap(homeBm, windowWidth*7/36, windowHeight*4/5-windowWidth/18, bitMapPaint);
            Bitmap leaderBm = BitmapFactory.decodeResource(r, R.drawable.gameover_leaderboard);
            leaderBm = Bitmap.createScaledBitmap(leaderBm, windowWidth/9, windowWidth/9, false);
            canvas.drawBitmap(leaderBm, windowWidth*4/9, windowHeight*4/5-windowWidth/18, bitMapPaint);
            Bitmap fbBm = BitmapFactory.decodeResource(r, R.drawable.gameover_facebook);
            fbBm = Bitmap.createScaledBitmap(fbBm, windowWidth/9, windowWidth/9, false);
            canvas.drawBitmap(fbBm, windowWidth*25/36, windowHeight*4/5-windowWidth/18, bitMapPaint);
        }
    }

    public void uploadScore(int score) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.147.208.46/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostScoreApi postScoreApi = retrofit.create(PostScoreApi.class);
        String name = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString("username", "defaultStringIfNothingFound");
        String userId = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString("userId", "defaultStringIfNothingFound");

        Call<PostScore> postScoreCall = postScoreApi.postScore(userId, name, score);
        postScoreCall.enqueue(new Callback<PostScore>() {
            @Override
            public void onResponse(Call<PostScore> call, Response<PostScore> response) {
                Log.d("Tag", "Post Score Success");
            }

            @Override
            public void onFailure(Call<PostScore> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

    public void checkUpload()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.147.208.46/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String userId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("userId", "defaultStringIfNothingFound");
        userId = "\"" + userId + "\"";

        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> userCall = userApi.getUser(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                User currentUser = response.body();
                if (currentUser != null) {
                    if(score>currentUser.getScore())
                        uploadScore(score);
                } else {
                    uploadScore(score);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("User Tag", t.getMessage());
            }
        });
    }
}
