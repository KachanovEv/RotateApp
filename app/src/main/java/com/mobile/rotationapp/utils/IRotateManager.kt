package com.mobile.rotationapp.utils

interface IRotateManager {

    var callback: RotateCallback?

    fun startListenSensors()

    fun stopListenSensors()
}
