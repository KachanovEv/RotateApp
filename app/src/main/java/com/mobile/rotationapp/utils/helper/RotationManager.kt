package com.mobile.rotationapp.utils.helper

import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.mobile.rotationapp.utils.IRotateManager
import com.mobile.rotationapp.utils.RotateCallback

class RotationManager(private val sensorManager: SensorManager?) : SensorEventListener,
    IRotateManager {

    private var azimuth: Int = 0
    private var roll: Int = 0
    private var pitch: Int = 0

    private var rotationSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var magnetometerSensor: Sensor? = null

    private var rotationMatrix = FloatArray(9)
    private var orientation = FloatArray(3)
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)

    private var isRotationSensor = false
    private var isAccelerometerSensor = false
    private var isMagnetometerSensor = false
    private var isAccelerometerSet = false
    private var isMagnetometerSet = false

    override var callback: RotateCallback? = null

    override fun onSensorChanged(event: SensorEvent) {
        var newAzimuth = 0
        var newPitch = 0
        var newRoll = 0
        when (event.sensor?.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                newAzimuth = (Math.toDegrees(
                    SensorManager.getOrientation(
                        rotationMatrix,
                        orientation
                    )[0].toDouble()
                ) + 360).toInt() % 360

                newPitch = (Math.toDegrees(
                    SensorManager.getOrientation(
                        rotationMatrix,
                        orientation
                    )[1].toDouble()
                ) + 360).toInt() % 360

                newRoll = (Math.toDegrees(
                    SensorManager.getOrientation(
                        rotationMatrix,
                        orientation
                    )[2].toDouble()
                ) + 360).toInt() % 360


            }
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                isAccelerometerSet = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                isMagnetometerSet = true
            }
        }

        if (isAccelerometerSet && isMagnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
            newAzimuth = (Math.toDegrees(
                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )[0].toDouble()
            ) + 360).toInt() % 360

            newPitch = (Math.toDegrees(
                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )[1].toDouble()
            ) + 360).toInt() % 360

            newRoll = (Math.toDegrees(
                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )[2].toDouble()
            ) + 360).toInt() % 360
        }

        if (newAzimuth != azimuth) {
            callback?.onAzimuthChanged(azimuth, newAzimuth)
            azimuth = newAzimuth
        }

        if (newPitch != pitch) {
            callback?.onPitchChanged(newPitch)
            pitch = newPitch
        }

        if (newRoll != roll) {
            callback?.onRollChanged(newRoll)
            roll = newRoll
        }
    }

    override fun startListenSensors() {
        if (sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if (sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null ||
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null
            ) {
                callback?.sensorsUnavailable()
            } else {
                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

                isAccelerometerSensor = sensorManager.registerListener(
                    this,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
                isMagnetometerSensor = sensorManager.registerListener(
                    this,
                    magnetometerSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } else {
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            isRotationSensor =
                sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun stopListenSensors() {
        if (isAccelerometerSensor || isMagnetometerSensor) {
            sensorManager?.unregisterListener(this, accelerometerSensor)
            sensorManager?.unregisterListener(this, magnetometerSensor)
        } else {
            if (isRotationSensor)
                sensorManager?.unregisterListener(this, rotationSensor)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}
