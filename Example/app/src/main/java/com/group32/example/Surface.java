package com.group32.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class Surface extends SurfaceView implements SurfaceHolder.Callback{
    protected Chibicharacter chibi1;
    protected GameThread gameThread;
    public Surface(Context context) {
        super(context);

        // Dảm bảo GameSurface này có thể focus để điều khiển các sự kiện
        this.setFocusable(true);
        // đặt các sự kiện liên quan tới game
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBit1= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.chibi1);
        // Gan nhan vat ban dau la chibit1
        this.chibi1=new Chibicharacter(this,chibiBit1,300,300);
        // tao thread cho game va start'

        this.gameThread=new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.chibi1.draw(canvas);
    }

    public void update(){
        this.chibi1.update();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);
                // Luồng cha, cần phải tạm dừng chờ GameThread kết thúc.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
    // bắt sự kiện chạm vào màn hình
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            this.chibi1.MoveUp();
            return true;
        }
        return false;
    }
}
