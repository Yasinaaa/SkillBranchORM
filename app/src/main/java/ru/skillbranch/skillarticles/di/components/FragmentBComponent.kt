package ru.skillbranch.skillarticles.di.components

import dagger.Component
import dagger.Subcomponent
import ru.skillbranch.skillarticles.di.modules.FragmentAModule
import ru.skillbranch.skillarticles.di.modules.FragmentBModule
import ru.skillbranch.skillarticles.di.scopes.FragmentScope
import ru.skillbranch.skillarticles.example.FragmentA
import ru.skillbranch.skillarticles.example.FragmentB

@FragmentScope
//dependencies = [ActivityComponent::class],
@Subcomponent(modules = [FragmentBModule::class])
interface FragmentBComponent {
    fun inject(fragment: FragmentB)
}