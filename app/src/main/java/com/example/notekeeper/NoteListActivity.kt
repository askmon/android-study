package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivity(Intent(this@NoteListActivity, NoteActivity::class.java))
        }

        initializeDisplayContent()
    }

    private fun initializeDisplayContent() {
        val listNotes:ListView = findViewById(R.id.list_note)
        val notes:List<NoteInfo> = DataManager.getInstance().notes

        val adapterNotes:ArrayAdapter<NoteInfo> = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, notes)

        listNotes.adapter = adapterNotes

        listNotes.setOnItemClickListener{ parent, view, position, id ->
            var intent: Intent = Intent(this@NoteListActivity, NoteActivity::class.java)
            intent.putExtra(NoteActivity.NOTE_POSITION, position)
            startActivity(intent)
        }
    }

}
