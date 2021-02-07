package ru.alinadorozhkina.gbnoteapp.data.model.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import ru.alinadorozhkina.gbnoteapp.data.model.models.Note
import ru.alinadorozhkina.gbnoteapp.data.model.NoteResult
import ru.alinadorozhkina.gbnoteapp.data.model.errors.NoAuthException
import ru.alinadorozhkina.gbnoteapp.data.model.models.User

private const val NOTES_COLLECTION = "my_notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName}:"
    }

    private val db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUsersNotesCollection().document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        OnFailureListener { exception ->
                            Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                            value = NoteResult.Error(exception)
                            throw exception
                        }
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUsersNotesCollection().document(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        value =
                            NoteResult.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener { exeption ->
                        value = NoteResult.Error(exeption)
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUsersNotesCollection().addSnapshotListener { snapshot, error ->
                    value = error?.let { NoteResult.Error(it) }
                        ?: snapshot?.let { query ->
                            val notes = query.documents.map { document ->
                                document.toObject(Note::class.java)
                            }
                            NoteResult.Success(notes)
                        }
                }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let {
                User(
                    it?.displayName ?: "",
                    it.email ?: ""
                )
            }
        }

    private fun getUsersNotesCollection() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
            .document(firebaseUser.uid)
            .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()
}
