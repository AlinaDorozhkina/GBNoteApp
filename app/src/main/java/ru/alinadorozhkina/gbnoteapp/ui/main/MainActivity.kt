package ru.alinadorozhkina.gbnoteapp.ui.main

import android.icu.lang.UCharacter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var ui: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setSupportActionBar(ui.myToolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapter = NoteAdapter()
        ui.recycleViewForNotes.layoutManager = GridLayoutManager(this, 2)
        ui.recycleViewForNotes.adapter=adapter

        viewModel.viewState().observe(this, Observer<MainViewState> { state ->
            state?.let { adapter.notes = state.notes}
        })

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
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}