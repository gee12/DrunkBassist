package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Иван on 19.02.2015.
 */
public class BitmapModel extends Model {

    protected Bitmap bitmap;
    protected DimensionF destDimenF;
    protected PointF centerF;
    protected Rect srcRect;
    protected RectF destRectF;

    public BitmapModel() {
        init(null, 0, 0, new PointF(), 0);
    }

    public BitmapModel(Bitmap bitmap) {
        init(bitmap, bitmap.getWidth(), bitmap.getHeight(), new PointF(), 0);
    }

    public BitmapModel(Bitmap bitmap, PointF pos) {
        init(bitmap, bitmap.getWidth(), bitmap.getHeight(), pos, 0);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, int msec) {
        init(bitmap, destWidth, destHeight, new PointF(), msec);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight) {
        init(bitmap, destWidth, destHeight, new PointF(), 0);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, PointF pos, int msec) {
        init(bitmap, destWidth, destHeight, pos, msec);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        init(bitmap, destWidth, destHeight, pos, 0);
    }

    public void init(Bitmap bitmap, int destWidth, int destHeight, PointF pos, int msec) {
        this.pos = pos;
        this.bitmap = bitmap;
        this.destDimenF = new DimensionF(destWidth, destHeight);
        this.srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        resetCenter(pos);
        resetDestRectF(pos);
        this.isVisible = true;
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        this.offsetStep = new PointF();
        this.msec = msec;
    }

    /////////////////////////////////////////////////////////////////////////
    //

    public void drawModel(Canvas canvas) {
        if (canvas == null || !isVisible) return;
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);
    }

    public boolean isSamePositions(BitmapModel other, float exp) {
        return (Math.abs(this.centerF.x - other.getCenter().x) < exp
                && Math.abs(this.centerF.y - other.getCenter().y) < exp);
    }

    public void setRandomPositionInScene(SceneMask mask) {
        if (mask == null) return;

        RectF destRectF = mask.getDestRectF();
        Random rand = new Random();
        do {
            PointF pos = new PointF(
                    rand.nextFloat() * destRectF.width() + destRectF.left,
                    rand.nextFloat() * destRectF.height() + destRectF.top
            );
            setPosition(pos);
        } while (mask.getHitStatus(centerF) != SceneMask.HitStatus.IN_SCENE);
    }

    /////////////////////////////////////////////////////////////////////////
    // reset

    public void resetDestRectF(PointF pos) {
        destRectF = new RectF(pos.x, pos.y,
                pos.x + destDimenF.width, pos.y + destDimenF.height);
    }

    public void resetCenter(PointF pos) {
        centerF = new PointF(pos.x + destDimenF.width/2.f, pos.y + destDimenF.height/2.f);
    }

    public void resetDestDimension() {
        this.destDimenF = new DimensionF(bitmap.getWidth(), bitmap.getHeight());
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    @Override
    public void setPosition(PointF pos) {
        this.pos = pos;
        resetCenter(pos);
        resetDestRectF(pos);
    }

    @Override
    public void offset(float dx, float dy) {
        pos.offset(dx, dy);
        resetCenter(pos);
        resetDestRectF(pos);
    }

    @Override
    public void offset() {
        pos.offset(offsetStep.x, offsetStep.y);
        resetCenter(pos);
        resetDestRectF(pos);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setDestDimension(int w, int h) {
        this.destDimenF.set(w, h);
    }

    public void offsetDestDimension(int dw, int dh) {
        this.destDimenF.offset(dw, dh);
    }

    public void setOffsetStepFromMsec() {
        int d = 250;
        float dx = destDimenF.width / (float)(msec + d);
        float dy = destDimenF.height / (float)(msec + d);
        setOffsetStep(dx, dy);
    }

    /*
    * Offset model with center position
    */
    public void makeCenterOffset() {
        float dx = offsetStep.x;
        float dy = offsetStep.y;
        this.destDimenF.offset(-dx, -dy);
        offset(dx/2.f, dy/2.f);
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public Bitmap getBitmap() {
        return bitmap;
    }

    public RectF getDestRectF() {
        return destRectF;
    }

    public Rect getSrcRect() {
        return srcRect;
    }

    public DimensionF getDestDimension() {
        return destDimenF;
    }

    public PointF getCenter() {
        return centerF;
    }
}
