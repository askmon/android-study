package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat.startActivity

import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    companion object {
        const val NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION"
        const val POSITION_NOT_SET = -1
    }

    var note: NoteInfo? = null
    var isNewNote = true
    var notePosition = 0
    var isCancelling = false
    var originalNoteCourseId = ""
    var originalNoteTitle = ""
    var originalNoteText = ""

    private val spinner: Spinner
        get() {
            val spinnerCourses: Spinner = findViewById(R.id.spinner_courses)
            return spinnerCourses
        }

    private val title: EditText
        get() {
            val title: EditText = findViewById(R.id.text_note_title)
            return title
        }

    private val text: EditText
        get() {
            val text: EditText = findViewById(R.id.text_note_text)
            return text
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        val spinnerCourses: Spinner = spinner

        val courses:List<CourseInfo> = DataManager.getInstance().courses

        val adapterCourses:ArrayAdapter<CourseInfo> = ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        readDisplayStateValues()
        saveOriginalNoteValues()

        if (!isNewNote)
            displayNote(spinnerCourses, title, text)
    }

    private fun saveOriginalNoteValues() {
        if(isNewNote) return
        originalNoteCourseId = note!!.course.courseId
        originalNoteTitle = note!!.title
        originalNoteText = note!!.text
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
        val position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)
        isNewNote = position == POSITION_NOT_SET
        if(isNewNote) {
            createNewNote()
        } else
            note = DataManager.getInstance().notes[position]
        }

    private fun createNewNote() {
        val dm = DataManager.getInstance()
        notePosition = dm.createNewNote()
        note = dm.notes[notePosition]
    }

    override fun onPause() {
        super.onPause()
        if(isCancelling) {
            if(isNewNote) {
                DataManager.getInstance().removeNote(notePosition)
            } else {
                storePreviousNoteValues()
            }
        } else {
            saveNote()
        }
    }

    private fun storePreviousNoteValues() {
        val course = DataManager.getInstance().getCourse(originalNoteCourseId)
        note!!.course = course
        note!!.title = originalNoteTitle
        note!!.text = originalNoteText
    }

    private fun saveNote() {
        note!!.course = spinner.selectedItem as CourseInfo
        note!!.title = title.text.toString()
        note!!.text = text.text.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_send_email -> { sendEmail(); return true; }
            R.id.action_cancel -> {
                isCancelling = true
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendEmail() {
        val course: CourseInfo = spinner.selectedItem as CourseInfo
        val subject = title.text.toString()
        val emailText = "Checkout what I blebleble ${course.title} \n ${text.text}"
        val intent = Intent(Intent.ACTION_SEND).setType("message/rfc2822")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, emailText)
        startActivity(intent)
    }
}
