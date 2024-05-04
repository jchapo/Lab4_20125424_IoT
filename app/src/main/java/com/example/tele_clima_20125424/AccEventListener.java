package com.example.tele_clima_20125424;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccEventListener implements SensorEventListener {

    private SensorListenerCallback callback;

    public AccEventListener(SensorListenerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float accelerationTotal = (float) Math.sqrt(x * x + y * y + z * z);

            // Llamar al método de callback con la aceleración total
            if (callback != null) {
                callback.onAccelerationChanged(accelerationTotal);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se necesita implementar en este caso
    }

    // Interfaz de callback para comunicarse con la clase que implementa esta clase
    public interface SensorListenerCallback {
        void onAccelerationChanged(float accelerationTotal);
    }
}
