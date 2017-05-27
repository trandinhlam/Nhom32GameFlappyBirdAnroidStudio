package com.group32.example;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class GameThread extends Thread {
    private static final int WAITINGTIME=10;// Thời gian chờ giữa mỗi update là 10 milisecond
    private boolean running;
    private Surface gameSurface;
    private SurfaceHolder surfaceHolder;

    public GameThread(Surface gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        long startTime=System.nanoTime();
        while (running){
            Canvas canvas=null;
            try{// lấy ra đối tượng canvas và khóa nó lại, tránh thread khác sử dụng
                    canvas=this.surfaceHolder.lockCanvas();

                // đồng bộ hóa
                synchronized (canvas){
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }

            }catch(Exception e){

            }finally {
                if(canvas!=null){
                    // mở khóa cho canvas
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now=System.nanoTime(); // đơn vị nanosecond;
            // thời gian cập nhật lại giao diện game
            long waitTime = (now - startTime)/1000000;
            if(waitTime > WAITINGTIME) {
                waitTime = WAITINGTIME; // Millisecond.
            }
            System.out.print(" Wait Time="+ waitTime);

            try {
                // Ngừng chương trình một chút.
                this.sleep(waitTime);
            } catch(InterruptedException e)  {

            }
            startTime = System.nanoTime();
            System.out.print(".");

        }


    }
}
