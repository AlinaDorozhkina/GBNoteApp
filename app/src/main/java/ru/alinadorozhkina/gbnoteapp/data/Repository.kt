package ru.alinadorozhkina.gbnoteapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.alinadorozhkina.gbnoteapp.data.model.Color
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import java.util.*

object Repository {

    private val notesLiveData = MutableLiveData<MutableList<Note>>()
    private val notes: MutableList<Note> = mutableListOf(
        Note(
            id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.ORANGE,
            data = "31.12.2020"),
        Note(
            id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.BLUE,
            data = "31.12.2020")
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<MutableList<Note>> = notesLiveData

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
        Log.d("Repository", " notiesLiveData "+ notesLiveData.value.toString())
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }
}



