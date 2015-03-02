package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.gee12.drunkbassist.MyMatrix;

import java.util.Random;

/**
 * Created by Иван on 19.02.2015.
 */
public class BitmapModel extends Model {

    protected Bitmap bitmap;
    protected PointF centerF;
    protected MyMatrix matrix;

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
//        this.pos = pos;
        this.bitmap = bitmap;
        this.matrix = new MyMatrix();
        matrix.srcRect.setDimension(getSrcDimension());
        setDestDimension(destWidth, destHeight);
        setPosition(pos);
        resetCenter();
        this.isVisible = true;
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        this.msec = msec;
    }

    /////////////////////////////////////////////////////////////////////////
    //

    public void drawModel(Canvas canvas) {
        if (canvas == null || !isVisible) return;
        canvas.drawBitmap(bitmap, matrix.createMatrix(), paint);

    }

    public boolean isSamePositions(BitmapModel other, float exp) {
        return (Math.abs(this.getPosition().x - other.getPosition().x) < exp
                && Math.abs(this.getPosition().y - other.getPosition().y) < exp);
    }

    public boolean isSameCentres(BitmapModel other, float exp) {
        return (Math.abs(this.centerF.x - other.getCenter().x) < exp
                && Math.abs(this.centerF.y - other.getCenter().y) < exp);
    }

    public static boolean isSamePositions(PointF pos1, PointF pos2, float exp) {
        return (Math.abs(pos1.x - pos2.x) < exp
                && Math.abs(pos1.y - pos2.y) < exp);
    }

    public void setRandomPositionInScene(SceneMask mask) {
        if (mask == null) return;

        RectF maskRect = mask.getMatrix().getDestRect();
        Random rand = new Random();
        do {
            PointF newPos = new PointF(
//                    rand.nextFloat() * mask.getDestDimension().width + mask.getPosition().x,
//                    rand.nextFloat() * mask.getDestDimension().height + mask.getPosition().y
                    rand.nextFloat() * maskRect.width() + maskRect.left,
                    rand.nextFloat() * maskRect.height() + maskRect.top
            );
            setPosition(newPos);
        } while (mask.getHitStatus(centerF) != SceneMask.HitStatus.IN_SCENE);
    }

    public void rotate(float angle) {
        matrix.setRotate(angle);
    }

    public void skew(float kx, float ky) {
        matrix.setSkew(kx, ky);
    }

    /////////////////////////////////////////////////////////////////////////
    // reset

    public void resetCenter() {
        centerF = new PointF(matrix.destRect.centerX(), matrix.destRect.centerY());
    }

    public void resetDestDimension() {
        matrix.setDestRectDimension(getSrcDimension());
    }

    public DimensionF getSrcDimension() {
        return new DimensionF(bitmap.getWidth(), bitmap.getHeight());
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    public void setPivotPoint(float x, float y) {
        matrix.setPivot(x, y);
    }

    public void setPosition(PointF pos) {
        matrix.offsetDestRectTo(pos.x, pos.y);
        resetCenter();
    }

    public void setDestDimension(float width, float height) {
        matrix.setDestRectDimension(width, height);
        resetCenter();
    }

    public void offsetPosition(float dx, float dy) {
        matrix.offsetDestRect(dx, dy);
        resetCenter();
    }

    public void offsetPosition() {
        matrix.offsetDestRect();
        resetCenter();
    }

    public void setTransStep(PointF transStep) {
        matrix.setTransStep(transStep);
    }

    public void setScaleStepFromMsec(int delta) {
        DimensionF srcDim = getSrcDimension();
        float dx = srcDim.width / (float)(msec + delta);
        float dy = srcDim.height / (float)(msec + delta);
        matrix.setScaleStep(dx, dy);
    }

    public void offsetSkew(float dx, float dy) {
        matrix.setSkew(dx, dy);
    }

    /*
    * Offset model with center position
    */
    public void makeCenterScale() {
        PointF scaleStep = matrix.getScaleStep();
        float dx = scaleStep.x;
        float dy = scaleStep.y;
        matrix.destRect.inset(dx, dy);
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public PointF getPivotPoint() {
        return matrix.getPivot();
    }

    public PointF getPosition() {
        return matrix.getDestRect().leftTop;
    }

    public DimensionF getDestDimension() {
        return matrix.getDestRect().getDimension();
    }

    public PointF getCenter() {
        return centerF;
    }

    public MyMatrix getMatrix() {
        return matrix;
    }
}
