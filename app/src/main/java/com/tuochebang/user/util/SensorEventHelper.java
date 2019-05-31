package com.tuochebang.user.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import com.amap.api.maps2d.model.Marker;

public class SensorEventHelper implements SensorEventListener {
    private final int TIME_SENSOR = 100;
    private long lastTime = 0;
    private float mAngle;
    private Context mContext;
    private Marker mMarker;
    private Sensor mSensor;
    private SensorManager mSensorManager;

    public SensorEventHelper(Context context) {
        this.mContext = context;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(3);
    }

    public void registerSensorListener() {
        this.mSensorManager.registerListener(this, this.mSensor, 3);
    }

    public void unRegisterSensorListener() {
        this.mSensorManager.unregisterListener(this, this.mSensor);
    }

    public void setCurrentMarker(Marker marker) {
        this.mMarker = marker;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - this.lastTime >= 100) {
            switch (event.sensor.getType()) {
                case 3:
                    float x = (event.values[0] + ((float) getScreenRotationOnPhone(this.mContext))) % 360.0f;
                    if (x > 180.0f) {
                        x -= 360.0f;
                    } else if (x < -180.0f) {
                        x += 360.0f;
                    }
                    if (Math.abs(this.mAngle - x) >= 3.0f) {
                        if (Float.isNaN(x)) {
                            x = 0.0f;
                        }
                        this.mAngle = x;
                        if (this.mMarker != null) {
                            this.mMarker.setRotateAngle(360.0f - this.mAngle);
                        }
                        this.lastTime = System.currentTimeMillis();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static int getScreenRotationOnPhone(Context context) {
        switch (((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return -90;
            default:
                return 0;
        }
    }
}
