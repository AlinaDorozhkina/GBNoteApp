package ru.alinadorozhkina.gbnoteapp.ui.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityMainBinding
import ru.alinadorozhkina.gbnoteapp.ui.base.BaseActivity
import ru.alinadorozhkina.gbnoteapp.ui.noteActivity.NoteActivity
import ru.alinadorozhkina.gbnoteapp.ui.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {

    override val viewModel: MainViewModel
            by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override val ui: ActivityMainBinding
            by lazy { ActivityMainBinding.inflate(layoutInflater) }

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
        R.id.logout -> showLogoutDialog().let { true }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { dataNotes ->
            adapter.notes = dataNotes
        }
    }

    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance()
            .show(supportFragmentManager, LogoutDialog.TAG)
    }

    override fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}

class LogoutDialog : DialogFragment() {
    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context!!)
            .setTitle(R.string.logout_dialog_title)
            .setMessage(R.string.logout_dialog_message)
            .setPositiveButton(R.string.ok_bth_title) { _, _ -> (activity as LogoutListener).onLogout() }
            .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> dismiss() }
            .create()

    interface LogoutListener {
        fun onLogout()
    }
}