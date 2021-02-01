package ru.alinadorozhkina.gbnoteapp.ui.main

import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.ui.BaseViewState

class MainViewState(val notes: MutableList<Note>, error: Throwable? = null) :
    BaseViewState<MutableList<Note>?>(notes, error)