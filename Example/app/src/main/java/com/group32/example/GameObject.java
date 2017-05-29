package com.group32.example;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class GameObject {
    // 2 biến static lưu giới hạn màn hình muốn hiển thị
    public static int screenheight = Resources.getSystem().getDisplayMetrics().heightPixels*7/10;// height giới hạn
    public static int screenwidth = Resources.getSystem().getDisplayMetrics().widthPixels;// width toàn màn hình

    protected Bitmap image;

    protected int rowCount;
    protected int colCount;
    protected int WIDTH;
    protected int HEIGHT;
    protected int width;
    protected int height;
    protected int x;

    public void setY(int y) {
        this.y = y;
    }

    protected int y;
    protected int movingVectorX ;// vector có x=0 tức là vật sẽ di chuyển theo chiều thẳng đứng
    protected int movingVectorY ;// ban đầu object sẽ bay xuống
    protected float Velocity;// vận tốc ban đầu
    protected long lastDrawNanoTime =-1;
    protected int deltaTime;
    public GameObject(Bitmap image, int rowCount, int colCount, int x, int y) {
        this.rowCount = rowCount;
        this.image = image;
        this.colCount = colCount;
        this.x = x;
        this.y = y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height= this.HEIGHT/ rowCount;
    }

    protected Bitmap createSubImageAt(int row,int col){
        Bitmap subImage=Bitmap.createBitmap(image,col*width,row*height,width,height);
        return subImage;
    }
    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    public void update(){
        // lấy thời điểm hiện tại trừ đi thời điểm trước đó đã cập nhật
        long now= System.nanoTime();
        // nếu là lần đầu tiên vẽ thì now=Starttime;
        if(-1==lastDrawNanoTime){
            lastDrawNanoTime=now;
        }
        deltaTime= ( int ) (now-lastDrawNanoTime)/1000000;// khoảng thời gian cập nhật
        double tempdistance=Velocity*deltaTime*0.2645833333;// chuyển từ m/s sang pixel/milis
        float distance=(float)tempdistance;
        // vector đơn vị biểu thị chiều dài của 1 đơn vị vector
        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);
        // cập nhật vị trí mới của nhân vật, trong trường hợp tổng quát cần làm như thế này
        if(0!=movingVectorLength) {// nếu nhân vật không đứng yên
            this.x += (int) distance * movingVectorX / movingVectorLength;
            this.y += (int) distance * movingVectorY / movingVectorLength;
        }
    }
    public void stop(){
        this.Velocity = 0.0f;
    }
}
