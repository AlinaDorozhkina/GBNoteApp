package ru.alinadorozhkina.gbnoteapp.data.model.models

import android.os.Parcelable
import  kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: String = "",
    val data: String = "",
    val title: String = "",
    val note: String = "",
    val color: Color = Color.BLUE,
    val lastChanges: Date = Date()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class Color {
    BLUE,
    ORANGE,
    GREEN,
    YELLOW,
    RED
}

