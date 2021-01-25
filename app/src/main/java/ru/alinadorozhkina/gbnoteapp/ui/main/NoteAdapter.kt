package ru.alinadorozhkina.gbnoteapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ItemNoteBinding

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var notes: MutableList<Note> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ui: ItemNoteBinding = ItemNoteBinding.bind(itemView)
        fun bind(note: Note) {
            ui.textViewData.text = note.data
            ui.textViewDayOfTheWeek.text = "Пн"
            ui.textViewTitle.text = note.title
            itemView.setBackgroundColor(note.color)
                //ui.textViewContent.text = note.note
        }
    }
}