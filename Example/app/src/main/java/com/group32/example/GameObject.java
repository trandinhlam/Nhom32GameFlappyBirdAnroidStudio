package com.group32.example;

import android.graphics.Bitmap;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class GameObject {
    protected Bitmap image;

    protected int rowCount;
    protected int colCount;
    protected  int WIDTH;
    protected  int HEIGHT;

    protected  int width;


    protected  int height;
    protected int x;
    protected int y;

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
}
