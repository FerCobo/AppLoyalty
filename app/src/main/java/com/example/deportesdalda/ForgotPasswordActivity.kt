package com.example.deportesdalda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun send(view:View) {

        if (!TextUtils.isEmpty(binding.txtemail.text.toString())) {
            db.collection("users")
                .document(binding.txtemail.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        auth.sendPasswordResetEmail(binding.txtemail.text.toString())
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    binding.progressBar.visibility = View.VISIBLE
                                    Toast.makeText(this, "El email ha sido enviado", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Error al enviar el email", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "No existe una cuenta asociada a ese correo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}