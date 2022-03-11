package com.mobile.rotationapp.utils

interface RotateCallback {

    fun onAzimuthChanged(previousAzimuth: Int, newAzimuth: Int)

    fun onRollChanged(roll: Int)
    fun onPitchChanged(pitch: Int)

    fun sensorsUnavailable()
}
