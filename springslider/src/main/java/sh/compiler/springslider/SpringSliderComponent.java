package sh.compiler.springslider;

import android.animation.ValueAnimator;
import android.graphics.Paint;

public class SpringSliderComponent {
    protected int sliderColor;
    protected int sliderBackgroundColor;
    protected boolean sliderFocusCircleCustom = false;
    protected int sliderFocusCircleColor;

    protected Paint sliderFocusCirclePaint;
    protected Paint sliderPaint;
    protected Paint sliderBackgroundPaint;

    protected float sliderPosXInitial = 0;
    protected int sliderPosition;
    protected float sliderPosX = 0;
    protected int sliderMaxValue = 100;

    protected float sliderFocusCircleSize;
    protected float sliderFocusCircleFullSize;

    protected ValueAnimator sliderAnimator;

    public int getSliderPosition() {
        return sliderPosition;
    }

    public void setSliderPosition(int sliderPosition) {
        this.sliderPosition = sliderPosition;
    }

    public int getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(int sliderColor) {
        this.sliderColor = sliderColor;
    }

    public Paint getSliderPaint() {
        return sliderPaint;
    }

    public void setSliderPaint(Paint sliderPaint) {
        this.sliderPaint = sliderPaint;
    }

    public boolean isSliderFocusCircleCustom() {
        return sliderFocusCircleCustom;
    }

    public void setSliderFocusCircleCustom(boolean sliderFocusCircleCustom) {
        this.sliderFocusCircleCustom = sliderFocusCircleCustom;
    }

    public int getSliderFocusCircleColor() {
        return sliderFocusCircleColor;
    }

    public void setSliderFocusCircleColor(int sliderFocusCircleColor) {
        this.sliderFocusCircleColor = sliderFocusCircleColor;
    }

    public Paint getSliderFocusCirclePaint() {
        return sliderFocusCirclePaint;
    }

    public void setSliderFocusCirclePaint(Paint sliderFocusCirclePaint) {
        this.sliderFocusCirclePaint = sliderFocusCirclePaint;
    }

    public int getSliderBackgroundColor() {
        return sliderBackgroundColor;
    }

    public void setSliderBackgroundColor(int sliderBackgroundColor) {
        this.sliderBackgroundColor = sliderBackgroundColor;
    }

    public Paint getSliderBackgroundPaint() {
        return sliderBackgroundPaint;
    }

    public void setSliderBackgroundPaint(Paint sliderBackgroundPaint) {
        this.sliderBackgroundPaint = sliderBackgroundPaint;
    }

    public float getSliderPosXInitial() {
        return sliderPosXInitial;
    }

    public void setSliderPosXInitial(float sliderPosXInitial) {
        this.sliderPosXInitial = sliderPosXInitial;
    }

    public float getSliderPosX() {
        return sliderPosX;
    }

    public void setSliderPosX(float sliderPosX) {
        this.sliderPosX = sliderPosX;
    }

    public float getSliderFocusCircleSize() {
        return sliderFocusCircleSize;
    }

    public void setSliderFocusCircleSize(float sliderFocusCircleSize) {
        this.sliderFocusCircleSize = sliderFocusCircleSize;
    }

    public float getSliderFocusCircleFullSize() {
        return sliderFocusCircleFullSize;
    }

    public void setSliderFocusCircleFullSize(float sliderFocusCircleFullSize) {
        this.sliderFocusCircleFullSize = sliderFocusCircleFullSize;
    }

    public ValueAnimator getSliderAnimator() {
        return sliderAnimator;
    }

    public void setSliderAnimator(ValueAnimator sliderAnimator) {
        this.sliderAnimator = sliderAnimator;
    }

    public int getSliderMaxValue() {
        return sliderMaxValue;
    }

    public void setSliderMaxValue(int sliderMaxValue) {
        this.sliderMaxValue = sliderMaxValue;
    }

    protected int mSliderMinValue = 0;

    public int getSliderMinValue() {
        return mSliderMinValue;
    }

    public void setmSliderMinValue(int mSliderMinValue) {
        this.mSliderMinValue = mSliderMinValue;
    }

    public SpringSliderComponent() {
        this.sliderPosition = SpringSlider.MIDDLE;
    }
}