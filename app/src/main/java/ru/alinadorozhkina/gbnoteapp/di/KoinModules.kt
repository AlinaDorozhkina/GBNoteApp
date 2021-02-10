package ru.alinadorozhkina.gbnoteapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.providers.FireStoreProvider
import ru.alinadorozhkina.gbnoteapp.data.model.providers.RemoteDataProvider
import ru.alinadorozhkina.gbnoteapp.ui.main.MainViewModel
import ru.alinadorozhkina.gbnoteapp.ui.noteActivity.NoteViewModel
import ru.alinadorozhkina.gbnoteapp.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}
