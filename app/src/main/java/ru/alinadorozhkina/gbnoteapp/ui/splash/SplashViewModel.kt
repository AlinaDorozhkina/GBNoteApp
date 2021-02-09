package ru.alinadorozhkina.gbnoteapp.ui.splash

import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.errors.NoAuthException
import ru.alinadorozhkina.gbnoteapp.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever { user ->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuth = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}