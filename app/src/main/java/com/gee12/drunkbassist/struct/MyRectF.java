package com.gee12.drunkbassist.struct;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Иван on 02.03.2015.
 */
public class MyRectF extends RectF {

    public PointF leftTop;

    public MyRectF() {
        super();
        init(0,0);
    }

    public MyRectF(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
        init(left, top);
    }

    public MyRectF(RectF r) {
        super(r);
        init(r.left, r.top);
    }

    public MyRectF(Rect r) {
        super(r);
        init(r.left, r.top);
    }

    @Override
    public void set(float left, float top, float right, float bottom) {
        super.set(left, top, right, bottom);
        init(left, top);
    }

    @Override
    public void set(RectF src) {
        super.set(src);
        init(left, top);
    }

    @Override
    public void set(Rect src) {
        super.set(src);
        init(left, top);
    }

    @Override
    public void offset(float dx, float dy) {
        super.offset(dx, dy);
        init(left, top);
    }

    @Override
    public void offsetTo(float newLeft, float newTop) {
        super.offsetTo(newLeft, newTop);
        init(left, top);
    }

    public void setDimension(float width, float height) {
        set(left, top, left + width, top + height);
    }

    public void setDimension(DimensionF dim) {
        set(left, top, left + dim.width, top + dim.height);
    }

    public DimensionF getDimension() {
        return new DimensionF(width(), height());
    }

    public void init(float left, float top) {
        leftTop = new PointF(left, top);
    }
}
