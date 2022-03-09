package com.example.servizi.application

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class FTApplication: MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        registerActivityLifecycleCallbacks(mFTActivityLifecycleCallbacks)
        MultiDex.install(this)
    }

    init {
        instance = this
    }

    val mFTActivityLifecycleCallbacks = FTActivityLifecycleCallbacks()

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(mFTActivityLifecycleCallbacks)
    }

    companion object{
        private var instance: FTApplication? = null

        fun currentActivity(): Activity?{
            return instance!!.mFTActivityLifecycleCallbacks.currentActivity
        }
    }
}