package ru.skillbranch.skillarticles

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkMonitor
//import ru.skillbranch.skillarticles.di.components.DaggerAppComponent
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    companion object{
//        lateinit var appComponent: AppComponent
//        lateinit var activityComponent: ActivityComponent

        private var instance : App? = null

        fun applicationContext() : Context{
            return  instance!!.applicationContext
        }
    }

    @Inject
    lateinit var monitor: NetworkMonitor
    @Inject
    lateinit var preferences: PrefManager

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

//        appComponent = DaggerAppComponent.factory()//.builder()
//            .create(applicationContext)
//            .preferencesModule(PreferencesModule(applicationContext))
//            .networkUtilsModule(NetworkMonitor(applicationContext))
//            .build()
//        appComponent.inject(this)

        monitor.registerNetworkMonitor()

        //set saved night/day mode
//        val mode = if (PrefManager.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
//        else AppCompatDelegate.MODE_NIGHT_NO
//        AppCompatDelegate.setDefaultNightMode(mode)
    }
}