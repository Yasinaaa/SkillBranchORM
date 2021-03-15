package ru.skillbranch.skillarticles.di.components

import dagger.Subcomponent
import ru.skillbranch.skillarticles.di.modules.FragmentAModule
import ru.skillbranch.skillarticles.di.modules.dagger.scopes.FragmentScope
import ru.skillbranch.skillarticles.example.FragmentA

@FragmentScope
//dependencies = [ActivityComponent::class],
@Subcomponent(modules = [FragmentAModule::class])
interface FragmentAComponent {
    fun inject(fragment: FragmentA)
}