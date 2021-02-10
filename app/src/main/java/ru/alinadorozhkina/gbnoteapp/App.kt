package ru.alinadorozhkina.gbnoteapp

import androidx.multidex.MultiDexApplication
import org.koin.core.context.startKoin
import ru.alinadorozhkina.gbnoteapp.di.appModule
import ru.alinadorozhkina.gbnoteapp.di.mainModule
import ru.alinadorozhkina.gbnoteapp.di.noteModule
import ru.alinadorozhkina.gbnoteapp.di.splashModule

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule, splashModule, noteModule, mainModule)
        }
    }
}
