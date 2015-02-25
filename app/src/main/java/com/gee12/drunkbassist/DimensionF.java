package com.gee12.drunkbassist;

/**
 * Created by Иван on 25.02.2015.
 */
public class DimensionF {

    public float width;
    public float height;

    public DimensionF() {
        set(0, 0);
    }

    public DimensionF(float w, float h) {
        set(w, h);
    }

    public DimensionF(DimensionF d) {
        set(d.width, d.height);
    }

    public final void set(DimensionF d) {
        set(d.width, d.height);
    }

    public final void set(float w, float h) {
        width = w;
        height = h;
        checkIsNeganive();
    }

    public void offset(float w, float h) {
        width += w;
        height += h;
        checkIsNeganive();
    }

    public void setWidth(float w) {
        this.width = w;
        checkIsNeganive();
    }

    public void setHeight(float h) {
        this.height = h;
        checkIsNeganive();
    }

    public void checkIsNeganive() {
        if (width < 0) width = 0;
        if (height < 0) height = 0;
    }

    public final boolean equals(float w, float h) {
        return this.width == w && this.height == h;
    }

    public final boolean equals(Object obj) {
        return obj instanceof DimensionF
                && (obj == this || equals(((DimensionF)obj).width, ((DimensionF)obj).height));
    }

}
