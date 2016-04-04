package sh.compiler.springslider;

import android.view.View;

/**
 * Created by pontus on 2016-04-04.
 */
public interface SpringSliderEventListener {
    void onSliderReleased(View v);
    void onSliderDown(View v);
    void onSliderValueChanged(View v, int value);
}
