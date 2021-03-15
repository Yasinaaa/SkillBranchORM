package ru.skillbranch.skillarticles.di.components

//@ActivityScope
//@Subcomponent(modules = [ActivityModule::class, AssistedVmModule::class])
////dependencies = [AppComponent::class],
//interface ActivityComponent {
//
//    @Subcomponent.Factory
//    interface Factory{
//        fun create(@BindsInstance activity: TestActivity): ActivityComponent
//    }
//
//    fun inject(activity: TestActivity)
////    fun getPreferences(): PrefManager
////    fun getActivity(): TestActivity
//
//    //module: FragmentAModule
//    fun plusFragmentAComponent(): FragmentAComponent
//    fun plusFragmentBComponent(): FragmentBComponent
//
//}