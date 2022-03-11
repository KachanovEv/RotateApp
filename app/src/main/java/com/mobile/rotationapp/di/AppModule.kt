package com.mobile.rotationapp.di

import android.app.Application
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import com.mobile.rotationapp.data.shared_pref.SharedManager
import com.mobile.rotationapp.utils.IRotateManager
import com.mobile.rotationapp.utils.helper.RotationManager
import com.mobile.rotationapp.utils.helper.SessionsHelper
import com.mobile.rotationapp.view.fragment.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SharedManager(androidContext()) }
    single { SessionsHelper(get()) }
    single { provideRotationManager(get()) }
    single { provideSensorManager(get()) }

    viewModel { HomeViewModel(get(), get()) }

}

fun provideSensorManager(app: Application): SensorManager? {
    return ContextCompat.getSystemService(app, SensorManager::class.java)
}

fun provideRotationManager(sensorManager: SensorManager?): IRotateManager {
    return RotationManager(sensorManager)
}
