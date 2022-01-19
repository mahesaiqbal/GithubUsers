package com.mahesaiqbal.githubusers

import android.app.Application
import com.mahesaiqbal.githubusers.core.di.retrofitModule
import com.mahesaiqbal.githubusers.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MainApplication)

            // Modules
            modules(listOf(appModule, retrofitModule))
        }
    }
}