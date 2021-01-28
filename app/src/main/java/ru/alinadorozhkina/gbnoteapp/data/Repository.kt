package ru.alinadorozhkina.gbnoteapp.data

import ru.alinadorozhkina.gbnoteapp.data.model.Color
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import java.util.*

object Repository {
   private val notes: MutableList<Note> = mutableListOf(
        Note(id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.WHITE,
        data = "31.12.2020"),
        Note(id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.RED,
        data = "11.01.2021"),
        Note(id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.PINK,
            data ="31.12.2020"),
        Note(id = UUID.randomUUID().toString(),
            title = "Новый год",
            note = "Празднование Нового Года",
            color = Color.BLUE,
            data ="31.12.2020")
   )
     fun getNotes (): List <Note> = notes
}


