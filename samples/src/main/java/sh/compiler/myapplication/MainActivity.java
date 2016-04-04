package sh.compiler.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import sh.compiler.springslider.SpringSlider;

public class MainActivity extends AppCompatActivity {

    SpringSlider sliderThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderThree = (SpringSlider) findViewById(R.id.sliderThree);
        sliderThree.setOnValueChangedListener(new SpringSlider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                Log.d("HAH", "value changed: " + value);
            }
        });
    }
}
