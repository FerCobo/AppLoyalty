package com.example.deportesdalda

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Mi perfil")

        binding.emailEditText.text = DeportesDaldaApplication.prefs.getEmail()

        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(binding.emailEditText.text.toString())
            .get()
            .addOnSuccessListener {
                binding.nombreEditText.setText(it.get("nombre") as String?)
                binding.apellidosEditText.setText(it.get("apellidos") as String?)
        }
    }

    fun update(view: View){

        db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(binding.emailEditText.text.toString())
            .update(mapOf(
                "nombre" to binding.nombreEditText,
                "apellidos" to binding.apellidosEditText
            ))
            .addOnSuccessListener { Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()}
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error al actualizar", e) }
        finish()
    }

    fun delete(view: View){
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setTitle("Eliminar cuenta")
            setMessage("¿Estás seguro/a de que deseas eliminar tu cuenta?")
            setPositiveButton("Sí", DialogInterface.OnClickListener({ dialog, id -> yes() }))
            setNegativeButton("No", null)
        }.create().show()

    }

    private fun yes(){
        HomeActivity.MyClass.activity?.finish()
        val user = Firebase.auth.currentUser!!
        db.collection("users").document(binding.emailEditText.text.toString()).delete()
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        finish()
    }

    fun changePass(view: View){
        startActivity(Intent(this, ChangePassActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu4, menu)
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