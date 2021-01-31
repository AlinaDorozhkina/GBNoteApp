package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Color
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityNoteBinding
import ru.alinadorozhkina.gbnoteapp.ui.helpers.EXTRA_VALUE
import ru.alinadorozhkina.gbnoteapp.ui.helpers.TIME_DATA_FORMAT
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

private const val SAVE_DELAY = 2000L

class NoteActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_VALUE, note)
            return intent
        }
    }

    private var note: Note? = null
    private lateinit var ui: ActivityNoteBinding
    private lateinit var viewModel: NoteViewModel
    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(ui.root)

        note = intent.getParcelableExtra(EXTRA_VALUE)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setSupportActionBar(ui.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note != null) {
            SimpleDateFormat(TIME_DATA_FORMAT, Locale.getDefault()).format(note!!.lastChanges)
        } else {
            getString(R.string.new_note_title)
        }
        initView()
    }

    private fun initView() {
        if (note != null) {
            ui.titleNote.setText(note?.title ?: "")
            ui.noteBody.setText(note?.note ?: "")

            val color = when (note?.color) {
                Color.BLUE -> R.color.blue_dark
                Color.WHITE -> R.color.white
                Color.ORANGE -> R.color.orange_main
                else -> R.color.white
            }

            ui.toolbarNote.setBackgroundColor(resources.getColor(color))
        }
        ui.titleNote.addTextChangedListener(textChangeListener)
        ui.noteBody.addTextChangedListener(textChangeListener)
        ui.datePicker.setOnClickListener { setData() }
        ui.datePicker.addTextChangedListener(textChangeListener)
    }

    private fun triggerSaveNote() {
        if (ui.titleNote.text == null || ui.titleNote.text!!.length < 3) return


        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                title = ui.titleNote.text.toString(),
                note = ui.noteBody.text.toString(),
                lastChanges = Date()
            ) ?: createNewNote()

            if (note != null) viewModel.saveChanges(note!!)
        }, SAVE_DELAY)
    }



    private fun createNewNote(): Note =  Note(
            UUID.randomUUID().toString(),
            ui.datePicker.text.toString(),
            ui.noteBody.text.toString(),
            ui.titleNote.text.toString())


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setData() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                ui.datePicker.setText("" + dayOfMonth + "." + month + 1 + ". " + year)
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}