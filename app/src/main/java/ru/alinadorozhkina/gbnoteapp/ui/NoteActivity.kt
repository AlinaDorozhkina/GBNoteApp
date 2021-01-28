package ru.alinadorozhkina.gbnoteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityNoteBinding

class NoteActivity: AppCompatActivity() {

    companion object{
        const val EXTRA_VALUE= "extra value"
        fun getStartIntent (context: Context, note : Note?) : Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_VALUE, note)
            return intent
        }
    }

    private var note :Note? = null
    private lateinit var ui: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(ui.root)

        note = intent.getParcelableExtra(EXTRA_VALUE)
        setSupportActionBar(ui.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
