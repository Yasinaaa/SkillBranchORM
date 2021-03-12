package ru.skillbranch.skillarticles.example

import androidx.lifecycle.ViewModel
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import javax.inject.Inject

class TestViewModel @Inject constructor(val repository: RootRepository): ViewModel() {
}