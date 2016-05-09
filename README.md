# SpringSlider
A Material design-springslider component for Android.
Can be used for scrubbing audio tracks etc.
![](springslider.gif)


### Usage

#####XML:
```
Add xmlns:app namespace to your layout (xmlns:app="http://schemas.android.com/apk/res-auto")

 <sh.compiler.springslider.SpringSlider
       android:id="@+id/sliderOne"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:sliderBackgroundColor="#554499"
       app:sliderColor="#998811"
       app:sliderMinValue="-100"
       app:sliderMaxValue="100"/>
```
Attributes:
```
sliderPosition (enum) - Starting position of slider, defaults to center
sliderColor (color) - Slider-head color and focus circle color
sliderBackgroundColor (color) - Slider-bar color
sliderMinValue (integer) - Sliders minimum value (left)
sliderMaxValue (integer) - Sliders max value (right)

```

#####Programmatically
```
SpringSlider springSlider = new SpringSlider(Context context);
```
Attributes:
```
addSpringSliderEventListener - add an event listener (SpringSliderEventListener) that handles
(onSliderReleased, onSliderDown, onSliderValueChanged)

setSliderColor(int color)
setSliderBackgroundColor(int color)
setSliderFocusCircleColor(int color)
setsliderMaxValue(int maxValue)
setSliderMinValue(int minValue)

```

