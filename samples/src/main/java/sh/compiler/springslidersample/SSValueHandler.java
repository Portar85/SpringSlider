package sh.compiler.springslidersample;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pontus on 2016-03-31.
 */
public class SSValueHandler {
    private static final String TAG = SSValueHandler.class.toString();

    private Object tag;

    interface ValueHandlerListener {
        void onValueChanged(@Nullable Object tag, int value);
    }
    private int maxValue = 100;
    private int minValue = 0;
    private int mCurrentValue = 0;
    private int mCurrentValueIncrease = 0;
    private ValueHandlerListener mValueHandlerListener;

    private Handler mHandler;

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setValueListener(ValueHandlerListener valueHandlerListener) {
        this.mValueHandlerListener = valueHandlerListener;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void startValueChange(int currentValue) {
        Log.d(TAG, "current Value: " + currentValue);
        mCurrentValue = currentValue;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(taskValueChange);
    }

    private void changeValue(int value) {
        if (value < 0 && value + mCurrentValue < minValue) {
            mCurrentValue = minValue;
        } else if (value > 0 && value + mCurrentValue > maxValue) {
            mCurrentValue = maxValue;
        } else {
            mCurrentValue = mCurrentValue + value;
        }
        if (mValueHandlerListener != null) {
            mValueHandlerListener.onValueChanged(tag, mCurrentValue);
        }
    }

    Runnable taskValueChange = new Runnable() {
        @Override
        public void run() {
            changeValue(mCurrentValueIncrease);
            Log.d(TAG, "changing value");
            mHandler.postDelayed(this, 50);
        }
    };

    public void setCurrentValueIncrease(int value) {
        mCurrentValueIncrease = value;
    }

    public void stopValueChange() {
        mHandler.removeCallbacks(taskValueChange);
        mCurrentValueIncrease = 0;
    }
}
