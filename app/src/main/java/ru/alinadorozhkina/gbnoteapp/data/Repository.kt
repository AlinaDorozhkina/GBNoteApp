package ru.alinadorozhkina.gbnoteapp.data

import ru.alinadorozhkina.gbnoteapp.data.model.Note

object Repository {
    private val notes: MutableList<Note> = mutableListOf()

    init {
        notes.add(Note("31.12.2020", "Новый год", " Празднование Нового Года", 0xff9575cd.toInt()))
        notes.add(Note("11.01.2021", "Работа", "Нужно выйти на работу", 0xff9565cd.toInt()))
    }

    fun getNotes(): MutableList<Note> = notes


}


