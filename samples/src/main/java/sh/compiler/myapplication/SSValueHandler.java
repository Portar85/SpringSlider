package sh.compiler.myapplication;

import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by pontus on 2016-03-31.
 */
public class SSValueHandler {

    Object tag;

    interface ValueHandlerListener {
        void onValueChanged(@Nullable Object tag, int value);
    }

    private int mCurrentValue = 0;
    private int mCurrentValueIncrease = 0;
    private ValueHandlerListener mValueHandlerListener;

    private Handler mHandler;



    public void setValueListener(ValueHandlerListener valueHandlerListener) {
        this.mValueHandlerListener = valueHandlerListener;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void startValueChange(int currentValue) {
        mCurrentValue = currentValue;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(taskValueChange);
    }

    private void changeValue(int value) {
        mCurrentValue = mCurrentValue + value;
        if (mValueHandlerListener != null) {
            mValueHandlerListener.onValueChanged(tag, mCurrentValue);
        }
    }

    Runnable taskValueChange = new Runnable() {
        @Override
        public void run() {
            changeValue(mCurrentValueIncrease);
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
