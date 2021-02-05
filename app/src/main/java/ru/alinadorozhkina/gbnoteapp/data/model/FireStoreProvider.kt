package ru.alinadorozhkina.gbnoteapp.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*

private const val COLLECTION_NAME = "my_notes"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName}:"
    }

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(COLLECTION_NAME)

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "Note $note is saved")
                    value = NoteResult.Success(note)
                }.addOnFailureListener {
                    OnFailureListener { exeption ->
                        Log.d(TAG, "Error saving note $note, message: ${exeption.message}")
                        value = NoteResult.Error(exeption)
                    }
                }
        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            notesReference.document(id)
                .get()
                .addOnSuccessListener { snapshot ->
                    value =
                        NoteResult.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener { exeption ->
                    value = NoteResult.Error(exeption)
                }
        }

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            notesReference.addSnapshotListener { snapshot, error ->
                value = error?.let { NoteResult.Error(it) }
                    ?: snapshot?.let { query ->
                        val notes = query.documents.map { document ->
                            document.toObject(Note::class.java)
                        }
                        NoteResult.Success(notes)
                    }
            }
        }

}
