package ru.skillbranch.skillarticles.di.modules

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.skillbranch.skillarticles.data.repositories.IRepository
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import ru.skillbranch.skillarticles.ui.RootActivity


@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityModule {

    companion object {
        @Provides
        fun provideActivity(activity: Activity): RootActivity = activity as RootActivity
    }

}