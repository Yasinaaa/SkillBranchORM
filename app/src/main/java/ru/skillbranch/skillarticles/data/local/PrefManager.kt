package ru.skillbranch.skillarticles.data.local

import android.content.SharedPreferences
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.preference.PreferenceManager
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.data.delegates.PrefDelegate
import ru.skillbranch.skillarticles.data.delegates.PrefLiveDelegate
import ru.skillbranch.skillarticles.data.models.AppSettings

object PrefManager {

    internal val preferences : SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.applicationContext())
    }

    var isDarkMode by PrefDelegate(false)
    var isBigText by PrefDelegate(false)
    var isAuth by PrefDelegate(false)

    val isAuthLive: LiveData<Boolean> by PrefLiveDelegate("isAuth", false, preferences)

    var appSettings = MediatorLiveData<AppSettings>().apply {
        val isDarkModeLive: LiveData<Boolean> by PrefLiveDelegate("isDarkMode", false, preferences)
        val isBigTextLive: LiveData<Boolean> by PrefLiveDelegate("isBigText", false, preferences)
        value = AppSettings()

        addSource(isDarkModeLive){
            value = value!!.copy(isDarkMode = it)
        }

        addSource(isBigTextLive){
            value = value!!.copy(isBigText = it)
        }
    }.distinctUntilChanged()


    fun clearAll(){
        preferences.edit().clear().apply()
    }
}