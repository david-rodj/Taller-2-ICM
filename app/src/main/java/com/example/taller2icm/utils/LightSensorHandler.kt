package com.example.taller2icm.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class LightSensorHandler(private val context: Context) : SensorEventListener {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var onLightChanged: ((Boolean) -> Unit)? = null
    private var lastIsDark: Boolean? = null

    companion object {
        private const val DARK_THRESHOLD = 50f // Aumentado a 50 lux para mejor detección
    }

    fun startListening(onLightChanged: (Boolean) -> Unit) {
        this.onLightChanged = onLightChanged
        lightSensor?.let { sensor ->
            val registered = sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            Log.d("LightSensorHandler", "Sensor registrado: $registered")
            Log.d("LightSensorHandler", "Sensor de luz: ${sensor.name}")
        } ?: run {
            Log.e("LightSensorHandler", "El dispositivo NO tiene sensor de luz")
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("LightSensorHandler", "Sensor de luz detenido")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lux = it.values[0]
                val isDark = lux < DARK_THRESHOLD

                Log.d("LightSensorHandler", "Luz detectada: $lux lux - ¿Es oscuro?: $isDark")

                // Solo notificar si hay un cambio real
                if (lastIsDark != isDark) {
                    lastIsDark = isDark
                    onLightChanged?.invoke(isDark)
                    Log.d("LightSensorHandler", "CAMBIO DE ESTADO notificado: ${if (isDark) "OSCURO" else "CLARO"}")
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("LightSensorHandler", "Precisión del sensor cambió: $accuracy")
    }
}