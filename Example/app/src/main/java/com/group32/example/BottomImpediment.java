package com.group32.example;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Window;

import java.util.Random;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

    // chướng ngại vật, chỉ xuất hiện lần đầu ở 1 vị trí cố định
    // ta cần xác định vị trí cố định đó trước
public class BottomImpediment extends Impediment {
    private Surface gameSurface;
    public BottomImpediment(Surface gameSurface, Bitmap image) {
        // xác định vị trí xuất hiện đầu tiên, ngay góc dưới cùng bên phải
        super(image);// bitmap này chỉ có 1 hình
        this.gameSurface = gameSurface;
        this.y = GameObject.screenheight*Impediment.randomNum/10-Impediment.GROUND*2;// tung độ y của chướng ngại vật

    }
}
