package com.bitohur.foodapp

import android.app.Application
import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App() : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(AppModules.modules)
        }
    }
}
