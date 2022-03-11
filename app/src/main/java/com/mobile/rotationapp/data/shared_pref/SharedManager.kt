package com.mobile.rotationapp.data.shared_pref

import android.content.Context
import android.content.SharedPreferences

class SharedManager(private val context: Context) {

    companion object {
        const val PREFERENCES_NAME = "ROTATION_APP_PREFERENCES"
        const val SESSIONS_COUNT = "SESSIONS_COUNT"
        const val SESSIONS_DURATION_IN_Millis = "SESSIONS_DURATION_IN_Millis"
    }

    private fun get(): SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var sessionsCount: Int
        get() = get().getInt(SESSIONS_COUNT, 0)
        set(value) = get().edit()
            .putInt(SESSIONS_COUNT, value)
            .apply()

    var sessionsDurationInMillis: Long
        get() = get().getLong(SESSIONS_DURATION_IN_Millis, 0L)
        set(value) = get().edit()
            .putLong(SESSIONS_DURATION_IN_Millis, value)
            .apply()
}
