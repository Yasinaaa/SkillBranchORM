package ru.skillbranch.skillarticles.di.components

import dagger.Subcomponent
import ru.skillbranch.skillarticles.di.modules.FragmentBModule
import ru.skillbranch.skillarticles.di.modules.dagger.scopes.FragmentScope
import ru.skillbranch.skillarticles.example.FragmentB

@FragmentScope
//dependencies = [ActivityComponent::class],
@Subcomponent(modules = [FragmentBModule::class])
interface FragmentBComponent {
    fun inject(fragment: FragmentB)
}