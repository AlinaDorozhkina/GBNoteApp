package ru.alinadorozhkina.gbnoteapp.data

import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.data.model.providers.FireStoreProvider
import ru.alinadorozhkina.gbnoteapp.data.model.providers.RemoteDataProvider

object Repository {
    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

    fun getCurrentUser () = remoteDataProvider.getCurrentUser()
}



