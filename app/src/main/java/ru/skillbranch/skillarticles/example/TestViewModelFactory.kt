package ru.skillbranch.skillarticles.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class TestViewModelFactory @Inject constructor(val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vmProvider = viewModels[modelClass] ?: throw IllegalArgumentException("class $modelClass not found")
        return vmProvider.get() as T
    }
}