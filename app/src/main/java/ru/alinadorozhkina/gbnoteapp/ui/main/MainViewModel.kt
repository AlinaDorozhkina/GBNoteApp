package ru.alinadorozhkina.gbnoteapp.ui.main

import androidx.lifecycle.Observer
import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.data.model.NoteResult
import ru.alinadorozhkina.gbnoteapp.ui.BaseViewModel

class MainViewModel(val repository: Repository = Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return

            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}