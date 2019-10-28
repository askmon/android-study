package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
            startActivity(intent)
        }
    }

}
