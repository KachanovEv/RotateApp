package com.mobile.rotationapp.view.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.rotationapp.data.shared_pref.SharedManager
import com.mobile.rotationapp.utils.IRotateManager
import com.mobile.rotationapp.utils.RotateCallback

class HomeViewModel(
    private val sharedManager: SharedManager,
    private val rotationManager: IRotateManager,
) : ViewModel(), RotateCallback {

    var sessionsCount = 0
    val azimuthLiveData = MutableLiveData<Int>()
    val rollLiveData = MutableLiveData<Int>()
    val pitchLiveData = MutableLiveData<Int>()
    val sensorsUnavailableData = MutableLiveData<Boolean>()

    init {
        sessionsCount = sharedManager.sessionsCount
        rotationManager.callback = this
        rotationManager.startListenSensors()
    }

    fun onDestroy() {
        rotationManager.stopListenSensors()
    }

    override fun onAzimuthChanged(previousAzimuth: Int, newAzimuth: Int) {
        azimuthLiveData.value = newAzimuth
    }

    override fun onRollChanged(roll: Int) {
        rollLiveData.value = roll
    }

    override fun onPitchChanged(pitch: Int) {
        pitchLiveData.value = pitch
    }

    override fun sensorsUnavailable() {
        sensorsUnavailableData.value = false
    }
}
