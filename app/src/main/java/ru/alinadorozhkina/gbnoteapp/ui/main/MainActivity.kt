package ru.alinadorozhkina.gbnoteapp.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityMainBinding
import ru.alinadorozhkina.gbnoteapp.ui.BaseActivity
import ru.alinadorozhkina.gbnoteapp.ui.noteActivity.NoteActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override val ui: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val layoutRes: Int = R.layout.activity_main
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(ui.myToolbar)

        ui.addingButton.setOnClickListener { openNoteScreen(null) }

        adapter = NoteAdapter(object : OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })

        ui.recycleViewForNotes.layoutManager = GridLayoutManager(this, 2)
        ui.recycleViewForNotes.adapter = adapter
    }

    private fun openNoteScreen(note: Note?) {
        startActivity(NoteActivity.getStartIntent(this, note?.id))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.search -> {
            Snackbar.make(
                ui.rootLayout,
                R.string.search,
                Snackbar.LENGTH_SHORT
            ).show()
            true
        }
        R.id.settings -> {
            Snackbar.make(
                ui.rootLayout,
                R.string.settings,
                Snackbar.LENGTH_SHORT
            ).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }
}