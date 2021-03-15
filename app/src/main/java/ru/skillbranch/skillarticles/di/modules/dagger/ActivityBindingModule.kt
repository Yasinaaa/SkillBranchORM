package ru.skillbranch.skillarticles.di.modules.dagger

//import dagger.android.ContributesAndroidInjector

//@Module
//abstract class ActivityBindingModule {
//
////    @ActivityScope
////    @Provides
////    fun providePair(): Pair<String, String> = "inject" to "pair"
//
//    @ContributesAndroidInjector(modules = [
//        AssistedVmModule::class,
//        FragmentBindingModule::class
//    ])
//    @ActivityScope
//    abstract fun contributeActivity() : TestActivity
//
//    @Binds
//    @ActivityScope
//    abstract fun bindRootRepository(
//        repository: RootRepository
//    ): IRepository
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(TestViewModel::class)
//    @ActivityScope
//    abstract fun bindViewModel(vm: TestViewModel.Factory):
//            TestViewModelFactory.ViewModelAssistedFactory<out ViewModel>
//    //ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(ViewModelA::class)
//    @ActivityScope
//    abstract fun bindViewModelA(vm: ViewModelA.Factory):
//            TestViewModelFactory.ViewModelAssistedFactory<out ViewModel>
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(ViewModelB::class)
//    @ActivityScope
//    abstract fun bindViewModelB(vm: ViewModelB.Factory):
//            TestViewModelFactory.ViewModelAssistedFactory<out ViewModel>
//
////    @ActivityScope
////    @Binds
////    abstract fun bindVmFactory(factory: TestViewModelFactory): ViewModelProvider.Factory
//
////    fun bindRootRepository(
////        preferences: PrefManager,
////        network: RestService
////    ): RootRepository = RootRepository(preferences, network)
//
//    //IF NEED OTHER REPOSITORY
//    //    @ActivityScope
//    //    @Provides
//    //    fun provideRootRepository(
//    //        preferences: PrefManager,
//    //        network: RestService
//    //    ): ArticlesRepository = ArticlesRepository(preferences, network)
//    //    @ActivityScope
//    //    @Provides
//    //    fun provideRootRepository(
//    //        preferences: PrefManager,
//    //        network: RestService
//    //    ): ArticleRepository = ArticleRepository(preferences, network)
//    //etc
//
////    @ActivityScope
////    @Provides
////    fun provideTestViewModel(repository: RootRepository): TestViewModel =
////        TestViewModel(repository)
//
//    //IF NEED OTHER VM
//    //    @ActivityScope
//    //    @Provides
//    //    fun provideTestViewModel(repository: ArticlesRepository): ArticlesViewModel =
//    //            ArticlesViewModel(repository)
//
//    //    @ActivityScope
//    //    @Provides
//    //    fun provideTestViewModel(repository: ArticleRepository): ArticleViewModel =
//    //            ArticleViewModel(repository)
//    //etc
//
////    @Provides
////    fun provideTestActivity(activity: TestActivity): TestActivity = activity
//}