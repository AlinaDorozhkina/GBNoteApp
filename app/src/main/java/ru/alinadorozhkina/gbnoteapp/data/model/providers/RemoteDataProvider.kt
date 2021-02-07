package ru.alinadorozhkina.gbnoteapp.data.model.providers

import androidx.lifecycle.LiveData
import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.data.model.NoteResult
import ru.alinadorozhkina.gbnoteapp.data.model.models.User

interface RemoteDataProvider {

    fun saveNote(note: Note): LiveData<NoteResult>

    fun getNoteById(id: String): LiveData<NoteResult>

    fun subscribeToAllNotes(): LiveData<NoteResult>

    fun getCurrentUser(): LiveData<User?>


}