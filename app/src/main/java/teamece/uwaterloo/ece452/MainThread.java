package teamece.uwaterloo.ece452;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAX_FPS = 60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameScene gameScene;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder holder, GameScene gameScene) {
        super();
        this.surfaceHolder = holder;
        this.gameScene = gameScene;
    }

    @Override
    public void run() {
        long startTime;
        long timeMills = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = timeMills;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameScene.update();
                    this.gameScene.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMills = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMills;

            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount += 1;
            if (frameCount == MAX_FPS) {
                averageFPS = 1000 / ((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
