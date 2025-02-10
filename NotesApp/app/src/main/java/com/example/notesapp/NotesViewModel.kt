package com.example.notesapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper: NotesDatabaseHelper = NotesDatabaseHelper(application)
    private var adapter: NotesAdapter? = null

    fun setAdapter(adapter: NotesAdapter) {
        this.adapter = adapter
        loadNotes()
    }

    fun loadNotes() {
        Log.d("NotesViewModel", "Loading notes...")
        val allNotes = dbHelper.getAllNotes()
        Log.d("NotesViewModel", "Notes loaded: ${allNotes.size}")
        adapter?.setNotes(allNotes) ?: Log.e("NotesViewModel", "Adapter is null")
    }

    fun getNoteById(id: Int): Note? {
        return dbHelper.getNoteById(id)
    }

    fun updateNote(updatedNote: Note) {
        dbHelper.updateNote(updatedNote)
        loadNotes()
    }

    fun addNote(note: Note) {
        dbHelper.insertNote(note)
        loadNotes()
    }

    fun deleteNote(note: Note) {
        dbHelper.deleteNote(note.id)
        loadNotes()
    }
}
