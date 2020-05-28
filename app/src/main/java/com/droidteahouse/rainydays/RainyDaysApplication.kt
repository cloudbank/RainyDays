package com.droidteahouse.rainydays

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.droidteahouse.rainydays.di.AppComponent
import com.droidteahouse.rainydays.di.DaggerAppComponent

open class RainyDaysApplication : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent = DaggerAppComponent.factory().create(this)
}