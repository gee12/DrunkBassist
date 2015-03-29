package com.gee12.drunkbassist.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.gee12.drunkbassist.Utils;
import com.gee12.drunkbassist.struct.DimensionF;
import com.gee12.drunkbassist.struct.MyMatrix;

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

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight) {
        init(bitmap, destWidth, destHeight, new PointF(), 0);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, int msec) {
        init(bitmap, destWidth, destHeight, new PointF(), msec);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        init(bitmap, destWidth, destHeight, pos, 0);
    }

    public BitmapModel(Bitmap bitmap, int destWidth, int destHeight, PointF pos, int msec) {
        init(bitmap, destWidth, destHeight, pos, msec);
    }

    public void init(Bitmap bitmap, int destWidth, int destHeight, PointF pos, int msec) {
        this.bitmap = bitmap;
        this.matrix = new MyMatrix();
        this.centerF = new PointF();
        matrix.srcRect.setDimension(getSrcDimension());
        setDestDimension(destWidth, destHeight);
        setPosition(pos);
        resetCenter();
        this.isVisible = true;
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.msec = msec;
    }

    /////////////////////////////////////////////////////////////////////////
    //

    @Override
    public void drawModel(Canvas canvas) {
        if (canvas == null || !isVisible) return;
        canvas.drawBitmap(bitmap, matrix.buildMatrix(), paint);
    }

    public static boolean isNearPositions(PointF pos1, PointF pos2, float exp) {
        return (Math.abs(pos1.x - pos2.x) < exp
                && Math.abs(pos1.y - pos2.y) < exp);
    }

    /**
     *
     * @param mask
     */
    public void setRandomPositionInScene(SceneMask mask) {
        if (mask == null) return;

        RectF maskRect = mask.getMatrix().getDestRect();
        do {
            PointF newPos = new PointF(
                    Utils.Random.nextFloat() * maskRect.width() + maskRect.left,
                    Utils.Random.nextFloat() * maskRect.height() + maskRect.top
            );
            setPosition(newPos);
        } while (mask.getHitStatus(centerF) != SceneMask.PositionStatus.ON_SCENE);
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
        centerF.set(matrix.destRect.centerX(), matrix.destRect.centerY());
    }

    public void resetDestDimension() {
        matrix.setDestRectDimension(getSrcDimension());
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

    public void insetDestRect(float dx, float dy) {
        matrix.destRect.inset(dx, dy);
        // ?!
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

    public void setTransStep(PointF step) {
        matrix.setTransStep(step);
    }

    /**
     *
     * @param koef
     */
    public void setScaleStepFromMsec(float koef) {
        DimensionF srcDim = getSrcDimension();
        float dx = srcDim.width / (float)(msec) * koef;
        float dy = srcDim.height / (float)(msec) * koef;
        matrix.setScaleStep(dx, dy);
    }

    public void setRotateStep(float step) {
        matrix.setRotateStep(step);
    }

    public void offsetRotate(float da) {
        matrix.offsetRotate(da);
    }

    public void offsetRotate() {
        matrix.offsetRotate();
    }

    public void setSkew(float dx, float dy) {
        matrix.setSkew(dx, dy);
    }

    /**
    * Offset model with center position
    */
    public void makeCenterScale() {
        PointF scaleStep = matrix.getScaleStep();
        insetDestRect(scaleStep.x, scaleStep.y);
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public DimensionF getSrcDimension() {
        return new DimensionF(bitmap.getWidth(), bitmap.getHeight());
    }

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public PointF getAbsPivotPoint() {
        PointF pivot = getPivotPoint();
        PointF pos = getPosition();
        return new PointF(pos.x + pivot.x, pos.y + pivot.y);
    }
}
