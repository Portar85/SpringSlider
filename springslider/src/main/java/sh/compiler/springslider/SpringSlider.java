package sh.compiler.springslider;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by pontus on 2016-03-29.
 */
public class SpringSlider extends View implements View.OnTouchListener, SSValueHandler.ValueHandlerListener {

    public interface OnValueChangedListener {
        void onValueChanged(int value);
    }

    private static final String TAG = "SpringSlider";

    public static final int LEFT = 0;
    public static final int MIDDLE = 1;
    public static final int RIGHT = 2;

    private static final int SLIDER_MARGIN = 40;

    private static int ANIMATION_TIME = 200;

    protected final int mSliderPosition;

    protected int mSliderColor;
    protected Paint mSliderPaint;

    protected int mSliderFocusCircleColor;
    protected Paint mSliderFocusCirclePaint;

    protected int mSliderBackgroundColor;
    protected Paint mSliderBackgroundPaint;

    protected GestureDetector mGestureDetector;

    protected float mSliderPosXInitial = 0;
    protected float mSliderPosX = 0;

    protected float mSliderFocusCircleSize;
    protected float mSliderFocusCircleFullSize;

    protected boolean mIsTouchingView = false;

    protected ValueAnimator mSliderAnimator;
    protected ValueAnimator mFocusCircleEnlargeAnimator;
    protected ValueAnimator mFocusCircleShrinkAnimator;

    protected int mSliderMaxValue = 100;
    protected int mSliderMinValue = 0;

    protected int mSliderMinAcceleration = 0;
    protected int mSliderMaxAcceleration = 100;

    private int _canvasWidth = 0;
    private int _canvasHeight = 0;
    private int _center = 0;

    private OnValueChangedListener onValueChangedListener;

    private int mCurrentValue = 0;


    private SSValueHandler ssValueHandler;

    public SpringSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SpringSlider,
                0, 0);
        try {
            mSliderPosition = a.getInt(R.styleable.SpringSlider_sliderPosition, MIDDLE);
            defineSliderColors(context, a);
        } finally {
            a.recycle();
        }
        mGestureDetector = new GestureDetector(context, new TouchListener());
        setOnTouchListener(this);

        init();
    }

    protected int addAlphaToFocusColor(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    protected void init() {
        mSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSliderBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSliderFocusCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSliderPaint.setColor(mSliderColor);
        mSliderBackgroundPaint.setColor(mSliderBackgroundColor);
        mSliderFocusCircleColor = addAlphaToFocusColor(mSliderColor, 0.6f);
        mSliderFocusCirclePaint.setColor(mSliderFocusCircleColor);
        ssValueHandler = new SSValueHandler();
        ssValueHandler.setValueListener(this);
    }


    protected void defineSliderColors(Context context, TypedArray a) {
        if (a.hasValue(R.styleable.SpringSlider_sliderColor)) {
            mSliderColor = a.getColor(R.styleable.SpringSlider_sliderColor, Color.BLUE);
        } else {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(
                    android.R.color.holo_blue_light,
                    tv,
                    true
            );
            mSliderColor = tv.data;
        }
        if (a.hasValue(R.styleable.SpringSlider_sliderBackgroundColor)) {
            mSliderBackgroundColor = a.getColor(R.styleable.SpringSlider_sliderBackgroundColor, Color.BLUE);
        } else {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(
                    android.R.color.holo_green_light,
                    tv,
                    true
            );
            mSliderColor = tv.data;
        }
    }

    /**
     *
     * @return returns the current x value for the slider
     */
    public float getCurrentSliderPosition() {
        return mSliderPosX;
    }

    /**
     * Sets the color for the slider circle
     * @param color as an int representation
     */
    public void setSliderColor(int color) {
        mSliderColor = color;
        mSliderPaint.setColor(color);
        this.invalidate();
    }

    /**
     * Sets the color for the slider area
     * @param color as an int representation
     */
    public void setSliderBackgroundColor(int color) {
        mSliderBackgroundColor = color;
        mSliderBackgroundPaint.setColor(color);
        this.invalidate();
    }


    public void setSliderMaxValue(int maxValue) {
        this.mSliderMaxValue = maxValue;
    }

    public void setSliderMinValue(int minValue) {
        this.mSliderMinValue = minValue;
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        this.onValueChangedListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(150, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        switch (mSliderPosition) {
            case LEFT:
                mSliderPosX = 40;
                break;
            case MIDDLE:
                mSliderPosX = w/2;
                break;
            case RIGHT:
                mSliderPosX = w - 40;
        }
        mSliderPosXInitial = mSliderPosX;
        mSliderFocusCircleFullSize = h/2;
        _canvasWidth = w;
        _canvasHeight = h;
        _center = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "ONDRAW");
        canvas.drawRect(
                60f,
                _center - 6,
                _canvasWidth - SLIDER_MARGIN,
                _center + 6,
                mSliderBackgroundPaint
        );
        if (mIsTouchingView) {
            canvas.drawCircle(mSliderPosX, canvas.getHeight()/2, mSliderFocusCircleSize, mSliderFocusCirclePaint);
        }
        canvas.drawCircle(mSliderPosX, canvas.getHeight()/2, canvas.getHeight()/6, mSliderPaint);
    }

    private int getValueIncrease(int currentX) {
        int pixelRange;
        if (_canvasWidth - SLIDER_MARGIN == mSliderPosXInitial) {
            pixelRange = _canvasWidth;
        } else {
            pixelRange = _canvasWidth - SLIDER_MARGIN - (int) mSliderPosXInitial;
        }
        int wantedRange = mSliderMaxAcceleration - mSliderMinAcceleration;
        if (pixelRange == 0) {
            return mSliderMinAcceleration;
        }
        int val = (((currentX - (int) mSliderPosXInitial) * wantedRange) / pixelRange) + mSliderMinAcceleration;
        Log.d(TAG, "returning: " + val);
        return val;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        if (!result) {
            switch (event.getAction()) {
                //moving
                case MotionEvent.ACTION_MOVE:
                    if (event.getX() > SLIDER_MARGIN &&
                            event.getX() < _canvasWidth - SLIDER_MARGIN) {
                        mSliderPosX = event.getX();
                        ssValueHandler.setCurrentValueIncrease(getValueIncrease((int) event.getX()));

                    }
                    mIsTouchingView = true;
                    this.invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "ACTION_UP");
                    beginSliderAnimation();
                    beginShrinkFocusAnimation();
                    ssValueHandler.stopValueChange();
                    this.invalidate();
                    break;
            }
        }
        return result;
    }

    private void beginSliderAnimation() {
        if (mSliderAnimator != null && mSliderAnimator.isRunning()) {
            mSliderAnimator.cancel();
        }
        mSliderAnimator = ValueAnimator.ofFloat(mSliderPosX, mSliderPosXInitial);
        mSliderAnimator.setDuration(ANIMATION_TIME);
        mSliderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mSliderPosX = value;
                SpringSlider.this.invalidate();
            }
        });
        mSliderAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.end();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSliderAnimator.setInterpolator(new AccelerateInterpolator(1.0f));
        mSliderAnimator.start();
    }

    private void beginShrinkFocusAnimation() {
        if (mFocusCircleEnlargeAnimator.isRunning()) {
            mFocusCircleEnlargeAnimator.cancel();
        }
        mFocusCircleShrinkAnimator = ValueAnimator.ofFloat(mSliderFocusCircleSize, 0f);
        mFocusCircleShrinkAnimator.setDuration(ANIMATION_TIME);
        mFocusCircleShrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSliderFocusCircleSize = (float) animation.getAnimatedValue();
                SpringSlider.this.invalidate();
            }
        });
        mFocusCircleShrinkAnimator.start();
        mFocusCircleShrinkAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsTouchingView = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }


    @Override
    public void onValueChanged(int value) {
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(value);
        }
    }

    protected class TouchListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            beginFocusCircleEnlargeAnimation();
            ssValueHandler.startValueChange(mCurrentValue);
            return true;
        }

        private void beginFocusCircleEnlargeAnimation() {
            mFocusCircleEnlargeAnimator = ValueAnimator.ofFloat(mSliderFocusCircleSize, mSliderFocusCircleFullSize);
            mFocusCircleEnlargeAnimator.setDuration(ANIMATION_TIME);
            mFocusCircleEnlargeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSliderFocusCircleSize = (float) animation.getAnimatedValue();
                }
            });
            mFocusCircleEnlargeAnimator.start();
        }
    }
}
