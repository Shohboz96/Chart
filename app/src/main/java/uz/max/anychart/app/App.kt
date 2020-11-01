package uz.max.anychart.app

import android.app.Application
import androidx.multidex.MultiDex

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        instance = this
    }

    companion object{
        lateinit var instance : App
    }
}