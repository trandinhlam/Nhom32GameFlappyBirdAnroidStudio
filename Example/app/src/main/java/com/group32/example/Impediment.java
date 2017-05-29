package com.group32.example;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by TDLAM123 on 5/28/2017.
 */

public class Impediment extends GameObject{
    protected static final int GROUND=GameObject.screenheight/10;// khoảng cách từ màn hình tới mặt đất
    protected static final float VELOCITY=1.2f;
    protected static final int MINNUM=5;
    protected static final int MAXNUM=10;
    protected static int randomNum=MAXNUM;// số ngẫu nhiên phát sinh vị trí chướng ngại vật
    protected Bitmap Image1;
    public Impediment(Bitmap image) {
        super(image, 1, 1,0,0);// bitmap này chỉ có 1 hình
        this.Image1 = image;
        this.movingVectorX = -1; // chuyển động từ phải qua trái
        this.movingVectorY = 0;
        this.Velocity = VELOCITY; // vận tốc của chướng ngại vật
        //xác định tọa độ xuất hiện của ảnh
        this.x = GameObject.screenwidth;
    }
    //

    public void update(){
        super.update();
        // không cần cập nhật gì thêm vì vận tốc là hằng số
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap( this.Image1, x, y, null);

        this.lastDrawNanoTime=System.nanoTime();
    }

    public int getRandomNum() {
        return randomNum;
    }
}