package ru.alinadorozhkina.gbnoteapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.models.Color
import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ItemNoteBinding
import ru.alinadorozhkina.gbnoteapp.ui.helpers.getColorInt

interface OnItemClickListener {
    fun onItemClick(note: Note)
}

class NoteAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var notes: List<Note> = mutableListOf()
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

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ui: ItemNoteBinding = ItemNoteBinding.bind(itemView)

        fun bind(note: Note) {
            ui.textViewData.text = note.data
            ui.textViewTitle.text = note.title

            ui.container.setCardBackgroundColor(note.color.getColorInt(itemView.context))
            itemView.setOnClickListener { onItemClickListener.onItemClick(note) }
            ui.textViewContent.text = note.note
        }
    }
}