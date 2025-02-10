package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 9 // Updated version
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PHOTO_PATH = "photoPath" // New column for photo path
        const val COLUMN_AUDIO_PATH = "audioPath" // New column for audio path
        const val COLUMN_LOCATION = "location" // New column for location
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_CONTENT TEXT,"
                + "$COLUMN_PHOTO_PATH TEXT,"
                + "$COLUMN_AUDIO_PATH TEXT,"
                + "$COLUMN_LOCATION TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PHOTO_PATH TEXT")
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_AUDIO_PATH TEXT")
            //db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_LOCATION TEXT")
        }
    }

    fun insertNote(note: Note): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PHOTO_PATH, note.photoPath)
            put(COLUMN_AUDIO_PATH, note.audioPath)
            put(COLUMN_LOCATION, note.location)
        }

        return db.insert(TABLE_NAME, null, values)
    }

    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PHOTO_PATH, note.photoPath)
            put(COLUMN_AUDIO_PATH, note.audioPath)
            put(COLUMN_LOCATION, note.location)
        }

        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNote(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getNoteById(id: Int): Note? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_PHOTO_PATH, COLUMN_AUDIO_PATH, COLUMN_LOCATION),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val note = Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUDIO_PATH)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
            )
            cursor.close()
            note
        } else {
            cursor?.close()
            null
        }
    }

    fun getAllNotes(): List<Note> {
        val notesList = ArrayList<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUDIO_PATH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
                )
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notesList
    }
}
