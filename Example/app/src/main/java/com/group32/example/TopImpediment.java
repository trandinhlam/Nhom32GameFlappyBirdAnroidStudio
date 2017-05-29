package com.group32.example;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by TDLAM123 on 5/28/2017.
 */

public class TopImpediment extends Impediment {
    private static final int SLIT_DIMENTION=1200;// Khe hở giữa 2 chướng ngại
    private Surface gameSurface;
    public TopImpediment(Surface s,Bitmap image) {
        super(image);
        //Xoay Image 180 độ để ngược hướng với Bottom tương ứng, Flip theo chiều ngang
        this.Image1=FlipHorizontalBitmap(RotateBitmap(image,180));
        this.gameSurface = s;
        // tung độ y của chướng ngại vật
        this.y = GameObject.screenheight*(Impediment.randomNum-Impediment.MINNUM)/10-SLIT_DIMENTION-GROUND*2;
        // sau khi khởi tạo chướng ngại vật thì thay đổi biến randomnum cho lần tạo Bottom sau
        Impediment.randomNum = MINNUM+ (int)(Math.random() * (MAXNUM-MINNUM)+1);

    }
    private static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private static Bitmap FlipHorizontalBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.setScale(-1,1);
        matrix.postTranslate(source.getWidth(),0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
