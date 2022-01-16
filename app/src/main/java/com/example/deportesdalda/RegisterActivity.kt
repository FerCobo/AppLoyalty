package com.example.deportesdalda


import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.deportesdalda.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialise()
    }

    private fun initialise(){
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseReference = database.reference.child("users")
    }

    fun term(view: View){val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setTitle(R.string.title_term)
            setMessage(R.string.body_term)
            setPositiveButton("Aceptar", DialogInterface.OnClickListener({ dialog, id -> acept() }))
            setNegativeButton("Rechazar",DialogInterface.OnClickListener({ dialog, id -> cancel() }))
        }.create().show()
    }

    private fun acept(){
        binding.checkBox.isChecked = true
    }

    private fun cancel(){
        binding.checkBox.isChecked = false
    }

    fun register(view: View){
        createNewAccount()
    }

    private fun createNewAccount() {
        db = FirebaseFirestore.getInstance()

        if (!TextUtils.isEmpty(binding.nombreEditText.text.toString()) && !TextUtils.isEmpty(binding.apellidosEditText.text.toString())
            && !TextUtils.isEmpty(binding.emailEditText.text.toString()) && !TextUtils.isEmpty(binding.passwordEditText.text.toString())
            && !TextUtils.isEmpty((binding.password2EditText.text.toString()))) {
            checkEmailExistsOrNot()
        } else {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkEmailExistsOrNot(){
        db.collection("users")
            .document(binding.emailEditText.text.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Ya existe una cuenta asociada a ese correo", Toast.LENGTH_SHORT).show()
                } else {
                    ifEmailNotExist()
                }
            }
    }

    private fun ifEmailNotExist() {
        if (binding.passwordEditText.text.toString() == binding.password2EditText.text.toString()) {
            if (binding.checkBox.isChecked) {
                binding.progressBar.visibility = View.VISIBLE

                db.collection("users")
                    .document(binding.emailEditText.text.toString())
                    .set(
                        hashMapOf(
                            "nombre" to binding.nombreEditText.text.toString(),
                            "apellidos" to binding.apellidosEditText.text.toString(),
                            "puntos" to "0"
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Datos registrados correctamente", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error al registrar", e) }

                auth.createUserWithEmailAndPassword(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
                    .addOnCompleteListener(this) {
                        val user: FirebaseUser? = auth.currentUser!!
                        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, ContinueActivity::class.java))
                                finish()
                            }
                        }
                        val userDb = databaseReference.child(user?.uid.toString())
                        userDb.child("firstName").setValue(binding.nombreEditText.text.toString())
                        userDb.child("lastName").setValue(binding.apellidosEditText.text.toString())
                    }
            } else {
                Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
        }
    }
}





