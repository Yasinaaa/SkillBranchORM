package ru.skillbranch.skillarticles.example

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class TestViewModelFactory @Inject constructor(
    val viewModels: MutableMap<Class<out ViewModel>,
    ViewModelAssistedFactory<out ViewModel>>){

    fun create(
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ): AbstractSavedStateViewModelFactory{
        return object : AbstractSavedStateViewModelFactory(owner, defaultArgs){
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val vmProvider = viewModels[modelClass] ?:
                throw IllegalArgumentException("class $modelClass not found")
                return vmProvider.create(handle) as T
            }
        }
    }

    interface ViewModelAssistedFactory<T: ViewModel>{
        fun create(handle: SavedStateHandle): T
    }

    //ViewModelProvider.Factory {

//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        val vmProvider = viewModels[modelClass] ?: throw IllegalArgumentException("class $modelClass not found")
//        return vmProvider.get() as T
//    }
}