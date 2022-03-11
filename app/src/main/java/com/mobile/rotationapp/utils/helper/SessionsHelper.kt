package com.mobile.rotationapp.utils.helper

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import com.mobile.rotationapp.data.shared_pref.SharedManager
import java.util.concurrent.TimeUnit

class SessionsHelper(private val sharedManager: SharedManager) :
    DefaultLifecycleObserver {

    private var startTime: Long = System.currentTimeMillis()
    private var minSessionDurationInMillis: Long = TimeUnit.MINUTES.toMillis(10)
    val sessionsLiveData = MutableLiveData<Int>()

    init {
        if (sharedManager.sessionsDurationInMillis >= minSessionDurationInMillis) {
            setSessions()
        } else if (sharedManager.sessionsDurationInMillis >= TimeUnit.MINUTES.toMillis(5)) {
            startTime = sharedManager.sessionsDurationInMillis
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val sessionLength = System.currentTimeMillis() - startTime
        if (sessionLength >= minSessionDurationInMillis) {
            setSessions()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        val timer = object : CountDownTimer(minSessionDurationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                setSessions()
                start()
            }
        }
        timer.start()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        setSessionsDurationInMillis()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        setSessionsDurationInMillis()
    }

    private fun setSessionsDurationInMillis() {
        val sessionLength = System.currentTimeMillis() - startTime
        sharedManager.sessionsDurationInMillis = sessionLength
    }

    private fun setSessions() {
        var sessionsCount = sharedManager.sessionsCount
        sessionsCount += 1
        sharedManager.sessionsCount = sessionsCount
        sharedManager.sessionsDurationInMillis = 0
        Log.d("sessionsCount = ", sharedManager.sessionsCount.toString())
        sessionsLiveData.value = sessionsCount
        startTime = System.currentTimeMillis()
    }
}
