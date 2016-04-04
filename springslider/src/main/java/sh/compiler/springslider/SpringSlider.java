package sh.compiler.springslider;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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
public class SpringSlider extends View implements View.OnTouchListener {
    private static final String TAG = SpringSlider.class.toString();

    public static final int LEFT = 0;
    public static final int MIDDLE = 1;
    public static final int RIGHT = 2;

    private static final int SLIDER_MARGIN = 40;

    private static int ANIMATION_TIME = 200;
    protected final SpringSliderComponent springSliderComponent = new SpringSliderComponent();

    protected GestureDetector mGestureDetector;

    protected SpringSliderEventListener eventListener;

    protected boolean mIsTouchingView = false;

    protected ValueAnimator mFocusCircleEnlargeAnimator;
    protected ValueAnimator mFocusCircleShrinkAnimator;

    private int _canvasWidth = 0;

    private int _center = 0;

    /**
     * Default constructor for programmatic implementation
     * @param context Context
     */
    public SpringSlider(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new TouchListener());
        setOnTouchListener(this);
        springSliderComponent.setSliderColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        springSliderComponent.setSliderBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        init();
    }

    public SpringSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SpringSlider,
                0, 0);
        try {
            springSliderComponent.setSliderPosition(a.getInt(R.styleable.SpringSlider_sliderPosition, MIDDLE));
            defineSliderColors(context, a);
            springSliderComponent.setmSliderMinValue(a.getInt(R.styleable.SpringSlider_sliderMinValue, 0));
            springSliderComponent.setSliderMaxValue(a.getInt(R.styleable.SpringSlider_sliderMaxValue, 100));
        } finally {
            a.recycle();
        }
        mGestureDetector = new GestureDetector(context, new TouchListener());
        setOnTouchListener(this);

        init();
    }

    /**
     * Adds alpha to color by a factor between 0-1
     * @param color int-color
     * @param factor 0-1, where 1 is opaque
     * @return new color as an int
     */
    protected int addAlphaToFocusColor(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    protected void init() {
        springSliderComponent.setSliderPaint(new Paint(Paint.ANTI_ALIAS_FLAG));
        springSliderComponent.setSliderBackgroundPaint(new Paint(Paint.ANTI_ALIAS_FLAG));
        springSliderComponent.setSliderFocusCirclePaint(new Paint(Paint.ANTI_ALIAS_FLAG));
        springSliderComponent.getSliderPaint().setColor(springSliderComponent.getSliderColor());
        springSliderComponent.getSliderBackgroundPaint().setColor(springSliderComponent.getSliderBackgroundColor());
        springSliderComponent.setSliderFocusCircleColor(addAlphaToFocusColor(springSliderComponent.getSliderColor(), 0.6f));
        springSliderComponent.getSliderFocusCirclePaint().setColor(springSliderComponent.getSliderFocusCircleColor());
    }

    public int getMinValue() {
        return springSliderComponent.getSliderMinValue();
    }

    public int getMaxValue() {
        return springSliderComponent.getSliderMaxValue();
    }

    protected void defineSliderColors(Context context, TypedArray a) {
        if (a.hasValue(R.styleable.SpringSlider_sliderColor)) {
            springSliderComponent.setSliderColor(a.getColor(R.styleable.SpringSlider_sliderColor, Color.BLUE));
        } else {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(
                    android.R.color.holo_blue_light,
                    tv,
                    true
            );
            springSliderComponent.setSliderColor(tv.data);
        }
        if (a.hasValue(R.styleable.SpringSlider_sliderBackgroundColor)) {
            springSliderComponent.setSliderBackgroundColor(a.getColor(R.styleable.SpringSlider_sliderBackgroundColor, Color.BLUE));
        } else {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(
                    android.R.color.holo_green_light,
                    tv,
                    true
            );
            springSliderComponent.setSliderColor(tv.data);
        }
    }

    /**
     *
     * * Note that this does not return the current value
     *   but rather the position as an x-coordinate
     *
     * @return returns the current x value for the slider in px
     */
    public float getCurrentSliderPosition() {
        return springSliderComponent.getSliderPosX();
    }

    /**
     * Sets the color for the slider circle
     * @param color as an int representation
     */
    public void setSliderColor(int color) {
        springSliderComponent.setSliderColor(color);
        springSliderComponent.getSliderPaint().setColor(color);
        this.invalidate();
    }

    /**
     * Sets the color for the slider area
     * @param color as int
     */
    public void setSliderBackgroundColor(int color) {
        springSliderComponent.setSliderBackgroundColor(color);
        springSliderComponent.getSliderBackgroundPaint().setColor(color);
        this.invalidate();
    }

    /**
     * Sets the focus circle color
     * Take note that this overrides the default behaviour of focus color getting
     * the sliders color with an alpha-value
     * @param color int-color
     */
    public void setSliderFocusCircleColor(int color) {
        springSliderComponent.setSliderFocusCircleColor(color);
        springSliderComponent.getSliderFocusCirclePaint().setColor(color);
        springSliderComponent.setSliderFocusCircleCustom(true);
    }

    /**
     * Adds listener to slider events
     * @param eventListener SpringSliderEventlistener
     */
    public void addSpringSliderEventListener(@NonNull SpringSliderEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets the max value (Slider is all the way to the right)
     * @param maxValue an int representation of the slider max value
     */
    public void setSliderMaxValue(int maxValue) {
        this.springSliderComponent.setSliderMaxValue(maxValue);
    }

    /**
     * Sets the minimum value (Slider is all the way to the left)
     * @param minValue an int representation of the sliders min value
     */
    public void setSliderMinValue(int minValue) {
        this.springSliderComponent.setmSliderMinValue(minValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(150, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        switch (springSliderComponent.getSliderPosition()) {
            case LEFT:
                springSliderComponent.setSliderPosX(SLIDER_MARGIN);
                break;
            case MIDDLE:
                springSliderComponent.setSliderPosX(w / 2);
                break;
            case RIGHT:
                springSliderComponent.setSliderPosX(w - SLIDER_MARGIN);
        }
        springSliderComponent.setSliderPosXInitial(springSliderComponent.getSliderPosX());
        springSliderComponent.setSliderFocusCircleFullSize(h / 2);
        _canvasWidth = w;
        _center = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(
                60f,
                _center - 6,
                _canvasWidth - SLIDER_MARGIN,
                _center + 6,
                springSliderComponent.getSliderBackgroundPaint()
        );
        if (mIsTouchingView) {
            canvas.drawCircle(springSliderComponent.getSliderPosX(), canvas.getHeight()/2, springSliderComponent.getSliderFocusCircleSize(), springSliderComponent.getSliderFocusCirclePaint());
        }
        canvas.drawCircle(springSliderComponent.getSliderPosX(), canvas.getHeight()/2, canvas.getHeight()/6, springSliderComponent.getSliderPaint());
    }

    private int getConvertedValue(int currentX) {
        int pixelRange;
        if (_canvasWidth - SLIDER_MARGIN == springSliderComponent.getSliderPosXInitial()) {
            pixelRange = _canvasWidth;
        } else {
            pixelRange = _canvasWidth - SLIDER_MARGIN - (int) springSliderComponent.getSliderPosXInitial();
        }
        int wantedRange = springSliderComponent.getSliderMaxValue() - springSliderComponent.getSliderMinValue();
        if (pixelRange == 0) {
            return springSliderComponent.getSliderMinValue();
        }
        return (((currentX - (int) springSliderComponent.getSliderPosXInitial()) * wantedRange) / pixelRange) + springSliderComponent.getSliderMinValue();
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
                        springSliderComponent.setSliderPosX(event.getX());
                    }
                    mIsTouchingView = true;
                    if (eventListener != null) {
                        int currentVal = getConvertedValue((int) springSliderComponent.getSliderPosX());
                        eventListener.onSliderValueChanged(this, currentVal);
                    }
                    this.invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    beginSliderAnimation();
                    beginShrinkFocusAnimation();
                    if (eventListener != null) {
                        eventListener.onSliderReleased(this);
                    }
                    this.invalidate();
                    break;
            }
        }
        return result;
    }

    private void beginSliderAnimation() {
        if (springSliderComponent.getSliderAnimator() != null && springSliderComponent.getSliderAnimator().isRunning()) {
            springSliderComponent.getSliderAnimator().cancel();
        }
        springSliderComponent.setSliderAnimator(ValueAnimator.ofFloat(springSliderComponent.getSliderPosX(), springSliderComponent.getSliderPosXInitial()));
        springSliderComponent.getSliderAnimator().setDuration(ANIMATION_TIME);
        springSliderComponent.getSliderAnimator().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                springSliderComponent.setSliderPosX(value);
                SpringSlider.this.invalidate();
            }
        });
        springSliderComponent.getSliderAnimator().addListener(new Animator.AnimatorListener() {
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
        springSliderComponent.getSliderAnimator().setInterpolator(new AccelerateInterpolator(1.0f));
        springSliderComponent.getSliderAnimator().start();
    }

    private void beginShrinkFocusAnimation() {
        if (mFocusCircleEnlargeAnimator.isRunning()) {
            mFocusCircleEnlargeAnimator.cancel();
        }
        mFocusCircleShrinkAnimator = ValueAnimator.ofFloat(springSliderComponent.getSliderFocusCircleSize(), 0f);
        mFocusCircleShrinkAnimator.setDuration(ANIMATION_TIME);
        mFocusCircleShrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                springSliderComponent.setSliderFocusCircleSize((float) animation.getAnimatedValue());
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

    private void beginFocusCircleEnlargeAnimation() {
        mFocusCircleEnlargeAnimator = ValueAnimator.ofFloat(springSliderComponent.getSliderFocusCircleSize(), springSliderComponent.getSliderFocusCircleFullSize());
        mFocusCircleEnlargeAnimator.setDuration(ANIMATION_TIME);
        mFocusCircleEnlargeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                springSliderComponent.setSliderFocusCircleSize((float) animation.getAnimatedValue());
            }
        });
        mFocusCircleEnlargeAnimator.start();
    }

    protected class TouchListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            if (eventListener != null) {
                eventListener.onSliderDown(SpringSlider.this);
            } else if (BuildConfig.DEBUG) {
                Log.d(TAG, "No listener set to springslider. \n @see SpringSlider.addEventListener()");
            }
            beginFocusCircleEnlargeAnimation();
            return true;
        }


    }
}
