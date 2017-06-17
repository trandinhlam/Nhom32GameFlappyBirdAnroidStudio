package com.group32.example;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.Settings;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class Chibicharacter extends GameObject{
    private static final float UP_VELOCITY = 3.2f;// vận tốc khi bay lên
    private static final int ROW_UP_AND_DOWN = 2;// cá nhân nhân vật này chỉ đi từ dưới lên trên
    private Bitmap[] up_and_down;

    // vận tốc của nhân vật

    //private int colUsing;
    public static final int tmpscale = GameObject.screenheight/2;
    // gameSurface là mô phỏng toàn bộ màn hình của trò chơi trong một thời điểm
    public Chibicharacter(Surface gameSurface, Bitmap image, int x, int y) {
        super(Bitmap.createScaledBitmap(image,tmpscale,tmpscale,false), 4, 3, x, y); // bitmap có 4 cột và 3 dòng, tức 4*3=12 hình
        this.up_and_down = new Bitmap[colCount];
        for(int col = 0; col< this.colCount; col++ ) {
            this.up_and_down[col] = this.createSubImageAt(ROW_UP_AND_DOWN, col);
        }
        this.movingVectorX = 0; // lúc đầu khởi tạo chuyển động là đi xuống
        this.movingVectorY = 0; //
        this.Velocity = 0.0f;// vận tốc ban đầu rơi tự do là 0
    }
    // hàm này đổi chiều vector chuyển động
    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }
    public void update() {
        super.update();
        // Cập  nhật vận tốc sau khoảng thời gian deltaTime
        double deltavelocicy=deltaTime*9.81/1000;// gia tốc trọng trường nhân thời gians, vận tốc khi này tình bằng m/s
        if(1 == movingVectorY){// nếu chiều chuyển động là đi xuống thì cộng dồn vận tốc
            Velocity+= (float)deltavelocicy ; // Công thức vận tốc rơi tự do
        }
        else if(-1 == movingVectorY){// ngược lại đi lên thì trừ dồn vận tốc dần về 0
            Velocity-= (float)deltavelocicy;
        }

        // màn hình giới hạn khi chơi
        if(this.y>GameObject.screenheight-Impediment.GROUND){//khi chạm đáy thì nhân vật "die"
            this.movingVectorY=0;
        }
    }

    public void draw(Canvas canvas){
        Bitmap bitmap=this.up_and_down[0];
        canvas.drawBitmap(bitmap,x,y,null);
        this.lastDrawNanoTime=System.nanoTime();

    }


    public void setVelocity(float velocity) {
        Velocity = velocity;
    }
    // bay lên
    public void MoveUp() {
        this.setMovingVector(0,-1);// đặt hướng bay lên
        this.setVelocity(UP_VELOCITY);// vận tốc khởi tạo khi bay lên
    }

    public float getVelocity() {
        return Velocity;
    }


    public void resetPosition(int x,int y) {
            this.x=x;
            this.y=y;
    }
}
