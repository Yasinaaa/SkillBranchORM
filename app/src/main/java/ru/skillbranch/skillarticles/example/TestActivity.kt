package ru.skillbranch.skillarticles.example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
//import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.R
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity: AppCompatActivity() { //Dagger

//    @Inject
//    lateinit var injectPair: Pair<String, String>
//
//    @Inject
//    lateinit var preferences: PrefManager
//
//    @Inject
//    lateinit var repository: RootRepository
    val viewModel: TestViewModel by viewModels()
//    @Inject
//    lateinit var factory: TestViewModelFactory//ViewModelProvider.Factory
//    val viewModel: TestViewModel by viewModels { factory.create(this, intent.extras) }
    //@Inject
//    val viewModel: TestViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

//        App.activityComponent = DaggerAppComponent.builder()
//            .activityModule(ActivityModule(this))
//            .appComponent(App.appComponent)
//            .build()

//        App.activityComponent = App.appComponent.activityComponent.create(this)
            //.plusActivityComponent(ActivityModule(this))

//            injectDependency()

        btn_a.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentA())
                .commit()
        }
        btn_b.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentB())
                .commit()
        }
        btn_clear.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentById(R.id.container)!!)
                .commit()
        }
    }

    fun injectDependency(){
//        App.activityComponent.inject(this)
//        tv_text.text = "isDark Mode: ${preferences.isDarkMode} pref instance: ${System.identityHashCode(preferences)} \n" +
//                " inject field value: ${injectPair.first} ${injectPair.second} " +
//                " pair instance: ${System.identityHashCode(injectPair)}"
    }
}