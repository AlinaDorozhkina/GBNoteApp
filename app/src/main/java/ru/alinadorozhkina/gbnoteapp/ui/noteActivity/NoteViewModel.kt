package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import androidx.lifecycle.ViewModel
import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.Note

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {
    private var pendingNote: Note? = null // note который временно живет в активити

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() { // когда жизненый цикл viewmodel закончится, мы сохраним заметку если она  существует
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}