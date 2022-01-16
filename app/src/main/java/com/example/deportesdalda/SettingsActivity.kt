package com.example.deportesdalda

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import com.example.deportesdalda.DeportesDaldaApplication.Companion.prefs
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewLastName: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        MyToolbar().show(this, "Configuración")

        emailEditText = findViewById(R.id.emailEditText)
        textViewName = findViewById(R.id.textViewName)
        textViewLastName = findViewById(R.id.textViewLastName)

        val email = prefs.getEmail()
        emailEditText.text = email

        db = FirebaseFirestore.getInstance()
        db.collection("users").document(email).get().addOnSuccessListener {
            textViewName.setText(it.get("nombre") as String?)
            textViewLastName.setText(it.get("apellidos") as String?)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            R.id.nav_edit -> {
                startActivity(Intent(this, EditActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}