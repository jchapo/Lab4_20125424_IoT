package com.example.tele_clima_20125424;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MagEventListener implements SensorEventListener {

    private SensorListenerCallback callback;

    public MagEventListener(SensorListenerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Aquí puedes realizar cualquier lógica relacionada con el magnetómetro
            // Por ejemplo, calcular la intensidad del campo magnético total.

            // Llamar al método de callback con los valores del magnetómetro
            if (callback != null) {
                callback.onMagneticFieldChanged(x, y, z);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se necesita implementar en este caso
    }

    // Interfaz de callback para comunicarse con la clase que implementa esta clase
    public interface SensorListenerCallback {
        void onMagneticFieldChanged(float x, float y, float z);
    }
}
