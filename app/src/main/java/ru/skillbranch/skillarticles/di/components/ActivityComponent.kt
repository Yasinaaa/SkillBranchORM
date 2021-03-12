package ru.skillbranch.skillarticles.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.di.modules.ActivityModule
import ru.skillbranch.skillarticles.di.modules.FragmentAModule
import ru.skillbranch.skillarticles.di.modules.FragmentBModule
import ru.skillbranch.skillarticles.di.scopes.ActivityScope
import ru.skillbranch.skillarticles.example.TestActivity
import javax.inject.Singleton

@ActivityScope
@Component(modules = [ActivityModule::class])
//dependencies = [AppComponent::class],
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: TestActivity): ActivityComponent
    }

    fun inject(activity: TestActivity)
//    fun getPreferences(): PrefManager
//    fun getActivity(): TestActivity

    //module: FragmentAModule
    fun plusFragmentAComponent(): FragmentAComponent
    fun plusFragmentBComponent(): FragmentBComponent

}