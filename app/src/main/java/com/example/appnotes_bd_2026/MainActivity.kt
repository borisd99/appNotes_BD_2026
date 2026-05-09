package com.example.appnotes_bd_2026

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    // Дефинираме базата тук, за да е достъпна навсякъде
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Връзваме компонентите от дизайна
        val myListView = findViewById<ListView>(R.id.listView)
        val btnAdd = findViewById<Button>(R.id.button)
        val inputField = findViewById<EditText>(R.id.editTextNote)

        // 2. Инициализираме базата с ново име, за да избегнем стари конфликти
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes_final_db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        // 3. Функция за презареждане на списъка
        fun updateList() {
            val notesFromDb = database.noteDao().getAllNotes()
            val justTexts = notesFromDb.map { it.text }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, justTexts)
            myListView.adapter = adapter
        }

        // Зареждаме бележките още при стартиране
        updateList()

        // 4. Логика при натискане на бутона
        btnAdd.setOnClickListener {
            val userText = inputField.text.toString().trim()

            if (userText.isNotEmpty()) {
                // Записваме в SQLite
                database.noteDao().insertNote(Note(text = userText))

                // Чистим полето и обновяваме екрана
                inputField.text.clear()
                updateList()

                Toast.makeText(this, "Бележката е записана!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Моля, въведете текст!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
