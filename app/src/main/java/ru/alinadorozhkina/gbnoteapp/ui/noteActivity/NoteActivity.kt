package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import org.koin.android.viewmodel.ext.android.viewModel
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.models.Color
import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityNoteBinding
import ru.alinadorozhkina.gbnoteapp.ui.base.BaseActivity
import ru.alinadorozhkina.gbnoteapp.ui.helpers.format
import ru.alinadorozhkina.gbnoteapp.ui.helpers.getColorInt
import java.util.*


private const val SAVE_DELAY = 2000L
private const val EXTRA_VALUE = "extra value"

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    override val viewModel: NoteViewModel by viewModel()

    override val ui: ActivityNoteBinding
            by lazy { ActivityNoteBinding.inflate(layoutInflater) }

    override val layoutRes: Int = R.layout.activity_note

    companion object {
        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_VALUE, noteId)
            return intent
        }
    }

    private var note: Note? = null
    private var color: Color = Color.BLUE
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
        setSupportActionBar(ui.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_VALUE)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: run {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        ui.titleNote.addTextChangedListener(textChangeListener)
        ui.noteBody.addTextChangedListener(textChangeListener)
        ui.datePicker.setOnClickListener { setData() }
        ui.datePicker.addTextChangedListener(textChangeListener)

        ui.colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }
    }

    private fun initView() {
        note?.run {
            removeEditListener()
            if (title != ui.titleNote.text.toString()) {
                ui.titleNote.setText(title)
            }
            if (note != ui.noteBody.text.toString()) {
                ui.noteBody.setText(title)
            }
            setToolbarColor(color)

            setEditListener()

            supportActionBar?.title = lastChanges.format()
        }
        ui.datePicker.setOnClickListener { setData() }
    }

    private fun setToolbarColor(color: Color) {
        ui.toolbarNote.setBackgroundColor(color.getColorInt(this@NoteActivity))
    }

    private fun triggerSaveNote() {
        if (ui.titleNote.text == null || ui.titleNote.text!!.length < 3) return
        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                title = ui.titleNote.text.toString(),
                note = ui.noteBody.text.toString(),
                color = color,
                lastChanges = Date()
            ) ?: createNewNote()

            note?.let {
                viewModel.saveChanges(it)
            }
        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(
        UUID.randomUUID().toString(),
        ui.datePicker.text.toString(),
        ui.noteBody.text.toString(),
        ui.titleNote.text.toString(),
        color
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.menu_note, menu).let { true }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete_note -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (ui.colorPicker.isOpen) {
            ui.colorPicker.close()
        } else {
            ui.colorPicker.open()
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete)
            .setNegativeButton(R.string.negative_answer) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.positive_answer) { _, _ -> viewModel.deleteNote() }
            .show()
    }

    private fun setData() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                ui.datePicker.setText("$dayOfMonth ${monthOfYear + 1} $year")
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()

        this.note = data.note
        data.note?.let {
            color = it.color
        }
        initView()
    }

    private fun setEditListener() {
        ui.titleNote.addTextChangedListener(textChangeListener)
        ui.noteBody.addTextChangedListener(textChangeListener)
        ui.datePicker.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        ui.titleNote.removeTextChangedListener(textChangeListener)
        ui.noteBody.removeTextChangedListener(textChangeListener)
        ui.datePicker.removeTextChangedListener(textChangeListener)
    }

    override fun onBackPressed() {
        if (ui.colorPicker.isOpen) {
            ui.colorPicker.close()
        } else {
            super.onBackPressed()
        }
    }
}


