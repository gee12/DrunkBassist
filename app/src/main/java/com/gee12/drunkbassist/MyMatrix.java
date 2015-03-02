package com.gee12.drunkbassist;

import android.graphics.Matrix;
import android.graphics.PointF;

import com.gee12.drunkbassist.struct.DimensionF;

/**
 * Created by Иван on 01.03.2015.
 */
public class MyMatrix extends Matrix {

    public MyRectF srcRect;
    public MyRectF destRect;

    private PointF transStep;
    private PointF scaleStep;

    private float rotate;
    private float rotateStep;

    private PointF skew;
    private PointF skewStep;

    private PointF pivot;

    public MyMatrix() {
        init(new MyRectF(), new MyRectF(), new PointF());
    }

    public MyMatrix(MyRectF srcRect, MyRectF destRect, PointF pivot) {
        init(srcRect, destRect, pivot);
    }

    public void init(MyRectF srcRect, MyRectF destRect, PointF pivot) {
        this.srcRect = srcRect;
        this.destRect = destRect;
        this.transStep = new PointF();
        this.scaleStep = new PointF();
        this.skew = new PointF();
        this.skewStep = new PointF();
        this.pivot = pivot;
    }

    public Matrix createMatrix() {
        reset();
        setRectToRect(srcRect, destRect, ScaleToFit.FILL);
        postRotate(rotate, pivot.x, pivot.y);
        preSkew(skew.x, skew.y, pivot.x, pivot.y);
        return this;
    }

    public void setSrcRect(MyRectF rect) {
        this.srcRect = rect;
    }

    public MyRectF getSrcRect() {
        return srcRect;
    }

    public MyRectF getDestRect() {
        return destRect;
    }

    public void setTransStep(PointF transStep) {
        this.transStep = transStep;
    }

    public void offsetDestRect() {
        destRect.offset(transStep.x, transStep.y);
    }

    public void offsetDestRect(float dx, float dy) {
        destRect.offset(dx, dy);
    }

    public void offsetDestRectTo(float dx, float dy) {
        destRect.offsetTo(dx, dy);
    }

    public void setDestRectDimension(float width, float height) {
        destRect.setDimension(width, height);
    }
    public void setDestRectDimension(DimensionF dim) {
        destRect.setDimension(dim.width, dim.height);
    }

    public void setScaleStep(float sx, float sy) {
        this.scaleStep.set(sx, sy);
    }

    @Override
    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public void setRotateStep(float rotateStep) {
        this.rotateStep = rotateStep;
    }

    public void setSkew(PointF skew) {
        this.skew = skew;
    }

    @Override
    public void setSkew(float x, float y) {
        skew.set(x, y);
    }

    public void offsetSkew(float dx, float dy) {
        skew.offset(dx, dy);
    }

    public void setSkewStep(float x, float y) {
        this.skewStep.set(x, y);
    }

    public void setPivot(float x, float y) {
        this.pivot = new PointF(x, y);
    }

    public PointF getPivot() {
        return pivot;
    }

    public PointF getTransStep() {
        return transStep;
    }

    public PointF getScaleStep() {
        return scaleStep;
    }

    public float getRotate() {
        return rotate;
    }

    public float getRotateStep() {
        return rotateStep;
    }

    public PointF getSkew() {
        return skew;
    }

    public PointF getSkewStep() {
        return skewStep;
    }
}
