package com.gee12.drunkbassist.struct;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.gee12.drunkbassist.R;

import static com.gee12.drunkbassist.struct.Hero.HeroFrames.MOVE1;
import static com.gee12.drunkbassist.struct.Hero.HeroFrames.nextFrame;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero extends Body {

    public enum HeroFrames {
        STAND(HeroStates.STAND, R.drawable.crossfire),
        MOVE1(HeroStates.MOVE, R.drawable.crossfire),
        MOVE2(HeroStates.MOVE, R.drawable.crossfire),
        AT_THE_EDGE1(HeroStates.AT_THE_EDGE, R.drawable.crossfire),
        AT_THE_EDGE2(HeroStates.AT_THE_EDGE, R.drawable.crossfire);

        public enum HeroStates {
            STAND,
            MOVE,
            AT_THE_EDGE
        }

        private int id;
        private HeroStates state;
        private Bitmap bitmap;

        HeroFrames(HeroStates state, int id) {
            this.state = state;
            this.id = id;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setBitmap(Resources resources) {
            this.bitmap = BitmapFactory.decodeResource(resources, id);
        }

        public static HeroFrames nextFrame(HeroFrames cur) {
            int next = cur.ordinal() + 1;
            return (next < values().length) ? values()[next] : values()[0];
        }

        public int getId() {
            return id;
        }

        public static HeroFrames valueOf(int resId) {
            for (HeroFrames frame : values()) {
                if (frame.getId() == resId)
                    return frame;
            }
            return null;
        }

        public static void setBitmaps(Resources resources) {
            for (HeroFrames frame : values()) {
                frame.setBitmap(resources);
            }
        }
    }

    public static int MOVE_MAX_COUNTER = 50;

    protected HeroFrames curFrame;
    protected int moveCounter = MOVE_MAX_COUNTER;

    public Limb lLeg;
    public Limb rLeg;
    public Limb hands;
    public Limb body;
    public Limb head;

    public Hero() {
        super();
        init();
    }

    public Hero(Bitmap bitmap) {
        super(bitmap);
        init();
    }

    public Hero(Bitmap bitmap, PointF pos) {
        super(bitmap, pos);
        init();
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
        init();
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
        init();
    }

    private void init() {

    }

    public void initLimbs(Resources res) {
        addLimb(lLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_l_leg), new PointF(10, 74)));
        addLimb(rLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_r_leg), new PointF(25, 74)));
        addLimb(body = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_body), new PointF(0, 33)));
        addLimb(head = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_head), new PointF(3, 0)));

//        DimensionF dim = getDestDimension();
//        for(Limb limb : limbs) {
//            limb.getPosition().offset(dim.width/6, dim.height/6);
//        }
    }

    @Override
    public void drawModel(Canvas canvas) {
        super.drawModel(canvas);
    }

    /////////////////////////////////////////////////////////////////////////
    // move

    public void move() {
        if (moveCounter-- < 0) {
            if (curFrame.id == MOVE1.id) {
                curFrame = nextFrame(curFrame);
                bitmap = curFrame.bitmap;
            }
            moveCounter = MOVE_MAX_COUNTER;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    public PointF getAbsPivotPoint() {
        PointF pivot = getPivotPoint();
        PointF pos = getPosition();
        return new PointF(pos.x + pivot.x, pos.y + pivot.y);
    }

}
