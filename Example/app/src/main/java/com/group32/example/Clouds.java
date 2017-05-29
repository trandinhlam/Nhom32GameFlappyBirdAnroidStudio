package com.group32.example;

import android.graphics.Bitmap;

/**
 * Created by TDLAM123 on 5/29/2017.
 */

public class Clouds extends Impediment {
    public Clouds(Bitmap image,int x) {
        super(image);
        this.y=0;
        this.x=x;// vị trí x do người khởi tạo quy định

    }
}
