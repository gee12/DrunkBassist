package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 17.02.2015.
 */
public class Body extends BitmapModel {

    protected List<FrameLimb> limbs;
    Bitmap split;
    Canvas bodyCanvas;

    public Body() {
        super();
        init();
    }

    public Body(Bitmap bitmap) {
        super(bitmap);
        init();
    }

    public Body(Bitmap bitmap, PointF pos) {
        super(bitmap, pos);
        init();
    }

    public Body(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
        init();
    }

    public Body(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
        init();
    }

    private void init() {
        this.limbs = new ArrayList<>();
//        DimensionF destDim = getDestDimension();
//        split = Bitmap.createBitmap((int)destDim.width, (int)destDim.height,
//                Bitmap.Config.ARGB_8888);
//        bodyCanvas = new Canvas(split);
    }

    public void addLimb(FrameLimb limb) {
        this.limbs.add(limb);
    }

    public void drawModel(Canvas canvas) {
        if (canvas == null || !isVisible) return;

        DimensionF destDim = getDestDimension();
        split = Bitmap.createBitmap((int)destDim.width, (int)destDim.height,
                Bitmap.Config.ARGB_8888);
        bodyCanvas = new Canvas(split);

        for(FrameLimb limb : limbs) {
            limb.drawModel(bodyCanvas);
        }

        canvas.drawBitmap(split, matrix.createMatrix(), paint);
    }

}
