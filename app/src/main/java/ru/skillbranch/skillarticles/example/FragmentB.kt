package ru.skillbranch.skillarticles.example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
//import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.fragment_a.tv_activity
import kotlinx.android.synthetic.main.fragment_b.*
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.di.modules.FragmentAModule
import ru.skillbranch.skillarticles.di.modules.FragmentBModule
import javax.inject.Inject

@AndroidEntryPoint
class FragmentB : Fragment() { //Dagger

    @Inject
    lateinit var activity: TestActivity

    @Inject
    lateinit var isBigText: String

    val viewModel: ViewModelB by viewModels()

//    @Inject
//    lateinit var factory: TestViewModelFactory
//
//    val viewModel: ViewModelB by viewModels{
//        factory.create(this, arguments)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        DaggerFragmentBComponent.builder()
//            .activityComponent(App.activityComponent)
//            .fragmentBModule(FragmentBModule())
//            .build()
//            .inject(this)
//        App.activityComponent
//            .plusFragmentBComponent() //FragmentBModule()
//            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_activity.text = "activity: ${activity.title} ${System.identityHashCode(activity)}"
        tv_is_big_text.text = "isBigText: $isBigText"

    }
}