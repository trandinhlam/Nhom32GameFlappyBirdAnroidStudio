package com.group32.example;

import android.graphics.Bitmap;

/**
 * Created by TDLAM123 on 5/29/2017.
 */

public class Ground extends Impediment {
    public Ground(Bitmap image,int xPosition) {
        super(image);
        this.y= GameObject.screenheight- GROUND;// tọa độ Y của mặt đất đã được quy định
        this.x=xPosition;// hoành độ x do người khởi tạo quy định
    }
}
