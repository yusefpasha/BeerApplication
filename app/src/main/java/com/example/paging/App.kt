package com.example.paging

import android.app.Application
import com.example.paging.di.beerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@App)

            modules(beerModule)
        }
    }
}