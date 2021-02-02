package ru.alinadorozhkina.gbnoteapp.ui.noteActivity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.Color
import ru.alinadorozhkina.gbnoteapp.data.model.Note
import ru.alinadorozhkina.gbnoteapp.databinding.ActivityNoteBinding
import ru.alinadorozhkina.gbnoteapp.ui.BaseActivity
import ru.alinadorozhkina.gbnoteapp.ui.helpers.TIME_DATA_FORMAT
import java.text.SimpleDateFormat
import java.util.*


private const val SAVE_DELAY = 2000L
private const val EXTRA_VALUE = "extra value"

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val layoutRes: Int = R.layout.activity_note

    companion object {
        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_VALUE, noteId)
            return intent
        }
    }

    private var note: Note? = null
    private lateinit var noteColor: Color
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

        val noteId = intent.getStringExtra(EXTRA_VALUE)
        noteId?.let {
            viewModel.loadNote(it)
        }
        if (noteId == null) supportActionBar?.title = getString(R.string.new_note_title)
    }

    private fun initView() {
        ui.titleNote.setText(note?.title ?: "")
        ui.noteBody.setText(note?.note ?: "")

        val color = when (note?.color) {
            Color.BLUE -> R.color.blue_dark
            Color.ORANGE -> R.color.orange_main
            Color.RED -> R.color.red
            Color.GREEN -> R.color.green
            Color.YELLOW -> R.color.yellow
            else -> R.color.white
        }
        ui.toolbarNote.setBackgroundColor(resources.getColor(color))

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

    private fun createNewNote(): Note = Note(
        UUID.randomUUID().toString(),
        ui.datePicker.text.toString(),
        ui.noteBody.text.toString(),
        ui.titleNote.text.toString(),
        noteColor
    )

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
                ui.datePicker.setText("$dayOfMonth ${monthOfYear + 1} $year")
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.radio_green ->
                    if (checked) {
                        noteColor = Color.GREEN
                    }
                R.id.radio_orange ->
                    if (checked) {
                        noteColor = Color.ORANGE
                    }
                R.id.radio_red ->
                    if (checked) {
                        noteColor = Color.RED
                    }
                R.id.radio_blue ->
                    if (checked) {
                        noteColor = Color.BLUE
                    }
                R.id.radio_yellow ->
                    if (checked) {
                        noteColor = Color.YELLOW
                    }
            }
        }
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }
}


