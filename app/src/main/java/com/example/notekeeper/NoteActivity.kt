package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    companion object {
        const val NOTE_INFO = "com.example.notekeeper.NOTE_INFO"
    }

    var note: NoteInfo? = null
    var isNewNote = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        val spinnerCourses:Spinner = findViewById(R.id.spinner_courses)

        val courses:List<CourseInfo> = DataManager.getInstance().courses

        val adapterCourses:ArrayAdapter<CourseInfo> = ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        readDisplayStateValues()

        val title: EditText = findViewById(R.id.text_note_title)
        val text: EditText = findViewById(R.id.text_note_text)

        if (!isNewNote)
            displayNote(spinnerCourses, title, text)
    }

    private fun displayNote(spinnerCourses: Spinner, title: EditText, text: EditText) {
        val courses: List<CourseInfo> = DataManager.getInstance().courses
        val courseIndex = courses.indexOf(note!!.course)
        spinnerCourses.setSelection(courseIndex)
        text.setText(note!!.text)
        title.setText(note!!.title)
    }

    private fun readDisplayStateValues() {
        val intent: Intent = intent
        note = intent.getParcelableExtra(NOTE_INFO)
        isNewNote = note == null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
