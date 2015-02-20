package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Иван on 19.02.2015.
 */
public class Model {

    protected Bitmap bitmap;
    protected int destWidth;
    protected int destHeight;
    private PointF pos;
    private PointF center;
    protected Rect srcRect;
    protected RectF destRectF;
    protected boolean isVisible;

    public Model() {
        init(null, 0, 0, new PointF());
    }

    public Model(Bitmap bitmap) {
        init(bitmap, bitmap.getWidth(), bitmap.getHeight(), new PointF());
    }

    public Model(Bitmap bitmap, int destWidth, int destHeight) {
        init(bitmap, destWidth, destHeight, new PointF());
    }

    public Model(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        init(bitmap, destWidth, destHeight, pos);
    }

    public void init(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        this.bitmap = bitmap;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        this.pos = pos;
        this.srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        resetCenter(pos);
        resetDestRectF(pos);
        this.isVisible = true;
    }

    /////////////////////////////////////////////////////////////////////////
    //

    public void drawModel(Canvas canvas, Paint p) {
        if (canvas == null && !isVisible) return;
        canvas.drawBitmap(bitmap, srcRect, destRectF, p);
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    public void resetDestRectF(PointF pos) {
        destRectF = new RectF(pos.x, pos.y,
                pos.x + destWidth, pos.y + destHeight);
    }

    public void resetCenter(PointF pos) {
        center = new PointF(pos.x + destWidth/2.f, pos.y + destHeight/2.f);
    }

    public void setPosition(PointF pos) {
        this.pos = pos;
        resetCenter(pos);
        resetDestRectF(pos);
    }

    public void setOffset(float dx, float dy) {
        pos.offset(dx, dy);
        resetCenter(pos);
        resetDestRectF(pos);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setDestWidth(int destWidth) {
        this.destWidth = destWidth;
    }

    public void setDestHeight(int destHeight) {
        this.destHeight = destHeight;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public Bitmap getBitmap() {
        return bitmap;
    }

    public PointF getPosition() {
        return pos;
    }

    public RectF getDestRectF() {
        return destRectF;
    }

    public Rect getSrcRect() {
        return srcRect;
    }

    public int getDestWidth() {
        return destWidth;
    }

    public int getDestHeight() {
        return destHeight;
    }

    public PointF getPos() {
        return pos;
    }

    public PointF getCenter() {
        return center;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
