package com.mobile.rotationapp.core

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.app.Application
import com.mobile.rotationapp.di.appModule

class RotateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RotateApplication)
            modules(appModule)
        }
    }
}
