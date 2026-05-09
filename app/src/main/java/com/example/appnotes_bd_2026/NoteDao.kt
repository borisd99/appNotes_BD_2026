package com.example.appnotes_bd_2026

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): List<Note>

    @Insert
    fun insertNote(note: Note)
}
