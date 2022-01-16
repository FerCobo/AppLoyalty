package com.example.deportesdalda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityEditAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAdminBinding

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Administrador")

        val bundle = intent.extras
        val emailEditText = bundle?.getString("email")
        db.collection("users").document(emailEditText.toString()).get().addOnSuccessListener {
            binding.textPoints.setText(it.get("puntos") as String?)
        }
    }

    fun recover(view: View){
        if (!TextUtils.isEmpty(binding.emailEditText.text.toString()) && !TextUtils.isEmpty(binding.pointsText.text.toString())) {
            db.collection("users")
                .document(binding.emailEditText.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("users")
                            .document(binding.emailEditText.text.toString())
                            .get().addOnSuccessListener {
                                binding.textPoints.setText(it.get("puntos") as String?)
                                binding.nameText.setText(it.get("nombre") as String?)
                                binding.lastNameText.setText(it.get("apellidos") as String?)
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "No existe una cuenta asociada a ese correo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }else {
            Toast.makeText(
                this,
                "Debes rellenar correctamente los campos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun update(view: View){
        if (!TextUtils.isEmpty(binding.emailEditText.text.toString()) && !TextUtils.isEmpty(binding.pointsText.text.toString())) {
            db.collection("users")
                .document(binding.emailEditText.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("users")
                            .document(binding.emailEditText.text.toString())
                            .update(mapOf(
                                "puntos" to binding.textPoints.text.toString()
                            ))
                            .addOnSuccessListener { Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show() }
                    } else {
                        Toast.makeText(
                            this,
                            "No existe una cuenta asociada a ese correo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }else {
            Toast.makeText(
                this,
                "Debes rellenar correctamente los campos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun clean (view: View){
        binding.emailEditText.setText("")
        binding.nameText.setText("")
        binding.lastNameText.setText("")
        binding.textPoints.setText("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}