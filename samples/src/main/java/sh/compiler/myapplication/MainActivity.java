package sh.compiler.myapplication;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sh.compiler.springslider.SpringSlider;
import sh.compiler.springslider.SpringSliderEventListener;

public class MainActivity extends AppCompatActivity implements SpringSliderEventListener, SSValueHandler.ValueHandlerListener {
    private static final String TAG = MainActivity.class.toString();

    SpringSlider sliderOne;
    SpringSlider sliderTwo;
    SpringSlider sliderThree;
    SpringSlider sliderFour;

    TextView textViewMinOne;
    TextView textViewMaxOne;
    TextView textViewCurrentOne;


    SSValueHandler valueHandlerOne;
    SSValueHandler valueHandlerTwo;
    SSValueHandler valueHandlerThree;
    SSValueHandler valueHandlerFour;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRoot);

        sliderOne = (SpringSlider) findViewById(R.id.sliderOne);
        textViewMinOne = (TextView) findViewById(R.id.minOne);
        textViewMaxOne = (TextView) findViewById(R.id.maxOne);
        textViewCurrentOne = (TextView) findViewById(R.id.currentOne);
        textViewMinOne.setText(String.format(getString(R.string.min), sliderOne.getMinValue()));
        textViewMaxOne.setText(String.format(getString(R.string.max), sliderOne.getMaxValue()));
        textViewCurrentOne.setText(String.format(getString(R.string.current), 0));

        sliderOne.addSpringSliderEventListener(this);
        sliderTwo = (SpringSlider) findViewById(R.id.sliderTwo);
        sliderTwo.addSpringSliderEventListener(this);
        sliderThree = (SpringSlider) findViewById(R.id.sliderThree);
        sliderThree.addSpringSliderEventListener(this);

        sliderFour = new SpringSlider(this);
        sliderFour.addSpringSliderEventListener(this);

        sliderFour.setId(getResources().getInteger(R.integer.id_programmatic));
        llRoot.addView(sliderFour);

        valueHandlerOne = new SSValueHandler();
        valueHandlerOne.setValueListener(this);
        valueHandlerOne.setTag(sliderOne);
        valueHandlerTwo = new SSValueHandler();
        valueHandlerTwo.setValueListener(this);
        valueHandlerTwo.setTag(sliderTwo);
        valueHandlerThree = new SSValueHandler();
        valueHandlerThree.setValueListener(this);
        valueHandlerThree.setTag(valueHandlerThree);
        valueHandlerFour = new SSValueHandler();
        valueHandlerFour.setValueListener(this);
        valueHandlerFour.setTag(valueHandlerFour);

    }

    @Override
    public void onSliderReleased(View v) {
        switch (v.getId()) {
            case R.id.sliderOne:
                valueHandlerOne.stopValueChange();
                break;
            case R.id.sliderTwo:
                valueHandlerTwo.stopValueChange();
                break;
            case R.id.sliderThree:
                valueHandlerThree.stopValueChange();
                break;
            case R.integer.id_programmatic:
                valueHandlerFour.stopValueChange();
                break;
        }
    }

    @Override
    public void onSliderDown(View v) {
        Log.d(TAG, "Slider Down");
        switch (v.getId()) {
            case R.id.sliderOne:
                valueHandlerOne.startValueChange(0);
                break;
            case R.id.sliderTwo:
                valueHandlerTwo.startValueChange(0);
                break;
            case R.id.sliderThree:
                valueHandlerThree.startValueChange(0);
                break;
            case R.integer.id_programmatic:
                valueHandlerFour.startValueChange(0);
                break;
        }
    }

    @Override
    public void onSliderValueChanged(View v, int value) {
        switch (v.getId()) {
            case R.id.sliderOne:
                valueHandlerOne.setCurrentValueIncrease(value);
                break;
            case R.id.sliderTwo:
                break;
            case R.id.sliderThree:
                break;
            case R.integer.id_programmatic:
                break;
        }
    }


    @Override
    public void onValueChanged(@Nullable Object tag, int value) {
        View v = (View) tag;
        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.sliderOne:
                textViewCurrentOne.setText(String.format(getString(R.string.current), value));
                break;
            case R.id.sliderTwo:
                break;
            case R.id.sliderThree:
                break;
            case R.integer.id_programmatic:
            break;
        }
    }
}
