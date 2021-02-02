package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.data.model.NoteResult
import ru.alinadorozhkina.gbnoteapp.ui.BaseViewModel

class NoteViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Note?, NoteViewState>() {
    private var pendingNote: Note? = null // note который временно живет в активити

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() { // когда жизненый цикл viewmodel закончится, мы сохраним заметку если она  существует
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return

                when (t) {
                    is NoteResult.Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                    is NoteResult.Error ->
                        viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }
}