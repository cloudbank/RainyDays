package com.droidteahouse.rainydays.di

import android.content.Context
import com.droidteahouse.rainydays.ui.home.HomeFragment
import com.droidteahouse.rainydays.ui.login.LoginFragment
import dagger.BindsInstance
import dagger.Component

import javax.inject.Singleton

// Scope annotation that the AppComponent uses
// Classes annotated with @Singleton will have a unique instance in this Component
@Singleton
// Definition of a Dagger component that adds info from the different modules to the graph
@Component(modules = [ViewModelModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(fragment: LoginFragment)
    fun inject(fragment: HomeFragment)


}
