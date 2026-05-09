package com.example.appnotes_bd_2026

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myListView = findViewById<ListView>(R.id.listView)
        val btnAdd = findViewById<Button>(R.id.button)
        val inputField = findViewById<EditText>(R.id.editTextNote)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes_final_db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        // Функция за презареждане на списъка
        fun updateList() {
            val notesFromDb = database.noteDao().getAllNotes()
            val justTexts = notesFromDb.map { it.text }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, justTexts)
            myListView.adapter = adapter
        }

        // Зареждане на бележките
        updateList()

        // Бутон
        btnAdd.setOnClickListener {
            val userText = inputField.text.toString().trim()

            if (userText.isNotEmpty()) {
                // Запис в SQLite
                database.noteDao().insertNote(Note(text = userText))

                inputField.text.clear()
                updateList()

                Toast.makeText(this, "Бележката е записана!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Моля, въведете текст!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
