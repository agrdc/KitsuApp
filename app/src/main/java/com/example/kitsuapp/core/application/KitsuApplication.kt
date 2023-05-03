package com.example.kitsuapp.core.application

import android.app.Application
import com.example.kitsuapp.domain.ValidatePasswordUseCase
import com.example.kitsuapp.domain.usecase.ValidateEmailUseCase
import com.example.kitsuapp.ui.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

class KitsuApplication : Application() {
    private val koinModule: Module = module {
        viewModel {
            LoginViewModel(get(), get())
        }
        factory { ValidateEmailUseCase() }
        factory { ValidatePasswordUseCase() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            //androidLogger()
            androidContext(this@KitsuApplication)
            modules(
                koinModule
            )
        }
        Timber.plant(Timber.DebugTree())
    }
}