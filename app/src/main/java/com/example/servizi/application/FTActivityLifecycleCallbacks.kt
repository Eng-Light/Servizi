package com.example.servizi.application

import android.app.Activity
import android.app.Application
import android.os.Bundle

class FTActivityLifecycleCallbacks:Application.ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        currentActivity = p0
    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        //TODO("Not yet implemented")
    }
}