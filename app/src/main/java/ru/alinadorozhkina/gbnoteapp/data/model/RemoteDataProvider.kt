package ru.alinadorozhkina.gbnoteapp.data.model

import androidx.lifecycle.LiveData

interface RemoteDataProvider {

    fun saveNote(note: Note): LiveData<NoteResult>

    fun getNoteById(id: String): LiveData<NoteResult>

    fun subscribeToAllNotes(): LiveData<NoteResult>


}