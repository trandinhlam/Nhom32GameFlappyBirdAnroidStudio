package com.group32.example;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.Settings;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class Chibicharacter extends GameObject{
    private static final int ROW_UP_AND_DOWN = 2;// cá nhân nhân vật này chỉ đi từ dưới lên trên
    private static final float MAXDISTANCE=400;// độ cao tối đa khi bật lên là 100, quá 100 sẽ rớt xuống lại
    private Bitmap[] up_and_down;
    // vận tốc của nhân vật
    private float Velocity=0.0f;// vận tốc ban đầu rơi tự do là 0.0
    private Surface gameSurface;
    private int colUsing;
    private int movingVectorX = 0;// vector có x=0 tức là vật sẽ di chuyển theo chiều thẳng đứng
    private int movingVectorY = 1;// ban đầu object sẽ bay xuống
    private long lastDrawNanoTime =-1;
    private float currentdistance=0;
    // gameSurface là mô phỏng toàn bộ màn hình của trò chơi trong một thời điểm
    //private GameSurface gamesurface;
    public Chibicharacter(Surface gameSurface, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);

        this.gameSurface= gameSurface;

        //this.topToBottoms = new Bitmap[colCount]; // 3
        //this.rightToLefts = new Bitmap[colCount]; // 3
        this.up_and_down = new Bitmap[colCount]; // 3
        //this.bottomToTops = new Bitmap[colCount]; // 3

        for(int col = 0; col< this.colCount; col++ ) {
            //this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            //this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.up_and_down[col] = this.createSubImageAt(ROW_UP_AND_DOWN, col);
            //this.bottomToTops[col]  = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
        }
    }
    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }
    public void update() {

        // lấy thời điểm hiện tại trừ đi thời điểm trước đó đã cập nhật
        long now= System.nanoTime();

        // nếu là lần đầu tiên vẽ thì now=Starttime;
        if(-1==lastDrawNanoTime){
            lastDrawNanoTime=now;
        }
        int deltaTime=(int ) (now-lastDrawNanoTime)/1000000;
        double tempdistance=Velocity*deltaTime*0.2645833333;// chuyển từ meter sang pixel
        float distance=(float)tempdistance;

        double temp=deltaTime*9.81/1000;

        Velocity+= (float)temp ; // Công thức vận tốc rơi tự do

        // vector đơn vị biểu thị chiều dài của 1 đơn vị
        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);
        // cập nhật vị trí mới của nhân vật, trong trường hợp tổng quát mới cần làm như thế này
        if(0!=movingVectorLength) {
            this.x += (int) distance * movingVectorX / movingVectorLength;
            this.y += (int) distance * movingVectorY / movingVectorLength;
        }
        //nếu nãy giờ là bay lên, và bay cao quá khoảng cách quy định thì rớt xuống
        if(movingVectorY<0) {
            currentdistance += distance;

            if (currentdistance > MAXDISTANCE) {
                currentdistance = 0;
                movingVectorY = -movingVectorY;
                setVelocity(0.0f);// vận tốc khi rơi
            }
        }
        // màn hình giới hạn khi chơi là 3/4 màn hình
        int screenheight= Resources.getSystem().getDisplayMetrics().heightPixels;
        if(this.y>screenheight*3/4){//khi chạm đáy thì nhân vật "die"
            this.y=screenheight*3/4;
            this.movingVectorY=0;
        }
    }

    public void draw(Canvas canvas){
        Bitmap bitmap=this.up_and_down[0];
        canvas.drawBitmap(bitmap,x,y,null);
        this.lastDrawNanoTime=System.nanoTime();
    }

    private void resetcurrentDistance(){
        currentdistance=0;
    }

    public void setVelocity(float velocity) {
        Velocity = velocity;
    }
    // bay lên
    public void MoveUp() {
        this.setMovingVector(0,-1);// đặt hướng bay lên
        this.resetcurrentDistance();// reset lại biến thời gian
        this.setVelocity(0.7f);// vận tốc khởi tạo khi bay lên
    }
}
