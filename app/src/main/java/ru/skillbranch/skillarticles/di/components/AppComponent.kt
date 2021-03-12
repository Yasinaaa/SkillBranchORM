package ru.skillbranch.skillarticles.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.di.modules.ActivityModule
import ru.skillbranch.skillarticles.di.modules.NetworkModule
import ru.skillbranch.skillarticles.di.modules.NetworkUtilsModule
import ru.skillbranch.skillarticles.di.modules.PreferencesModule

@Component(modules = [PreferencesModule::class, NetworkUtilsModule::class, NetworkModule::class])
interface AppComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(app: App)
//    fun getPreferences(): PrefManager
//    fun plusActivityComponent(actModule: ActivityModule): ActivityComponent

    val activityComponent: ActivityComponent.Factory
}