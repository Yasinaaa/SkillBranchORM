package ru.skillbranch.skillarticles.example

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
//import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import javax.inject.Inject

class ViewModelB //@AssistedInject
@ViewModelInject constructor(
    val repository: RootRepository,
    @Assisted val handle: SavedStateHandle
): ViewModel() {

//    @AssistedInject.Factory
//    interface Factory: TestViewModelFactory.ViewModelAssistedFactory<ViewModelB>
}