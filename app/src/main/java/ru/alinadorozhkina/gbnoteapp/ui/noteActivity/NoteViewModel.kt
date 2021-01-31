package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.alinadorozhkina.gbnoteapp.data.Repository
import ru.alinadorozhkina.gbnoteapp.data.model.Note

class NoteViewModel (private val repository : Repository= Repository) : ViewModel() {
    private var pendingNote : Note? = null // note который временно живет в активити

    fun saveNote (note: Note){
        pendingNote=note
    }

    override fun onCleared() { // когда жизненый цикл viewmodel закончится, мы сохраним заметку если она  существует
        if (pendingNote!=null){
            Log.d("Note view model", " есть несохраненная pendingNote ")
            repository.saveNote(pendingNote!!)
            Log.d("Note view model", " сохранили pendingNote ")
        }
    }
}