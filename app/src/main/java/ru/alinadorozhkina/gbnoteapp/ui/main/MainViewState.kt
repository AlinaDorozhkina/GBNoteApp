package ru.alinadorozhkina.gbnoteapp.ui.main

import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.ui.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)

