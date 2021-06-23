package com.miljanpeles.lib.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

import com.miljanpeles.lib.R;

/**
 * Kada god se koristi CopassView potrebno je da se u onResume i onPause activitya dodaju compassove onResume i onPause metode!
 * U XMLu moze da se definise custom slika za compass atributom android:src="@drawable/mojcompass"
 */
public class CompassView extends androidx.appcompat.widget.AppCompatImageView implements SensorEventListener {

    private static final String TAG = "CompassView";

    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float ugaoTS = 0f;
    private float ugaoCompassN = 0f;
    private float currect_ugao_CompassN = 0f;

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if(this.getDrawable() == null) setImageResource(R.drawable.compass21fw15def);
        init(context);
    }

    private void init(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void onResume() {
        boolean hasSensor = sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);

        if(!hasSensor) {
            Log.e(TAG, "Kompas ne moze biti ucitan jer telefon nema senzor za isti.");
        }
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2];
            }

            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                ugaoTS = (float) Math.toDegrees(orientation[0]);
                ugaoTS = (ugaoTS + 360) % 360;
                ugaoCompassN = ugaoTS;

                /*Animation anArrow = new RotateAnimation(-currect_ugaoTS, -ugaoTS, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                currect_ugaoTS = ugaoTS;
                anArrow.setDuration(500);
                anArrow.setRepeatCount(0);
                anArrow.setFillAfter(true);
                arrowView.startAnimation(anArrow);*/

                Animation anCompass = new RotateAnimation(-currect_ugao_CompassN, -ugaoCompassN, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                currect_ugao_CompassN = ugaoCompassN;
                anCompass.setDuration(500);
                anCompass.setRepeatCount(0);
                anCompass.setFillAfter(true);

                this.startAnimation(anCompass);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

}

