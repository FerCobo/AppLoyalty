package com.example.deportesdalda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityChangePassBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Modificar contraseña")

        binding.emailEditText.text = DeportesDaldaApplication.prefs.getEmail()

        binding.oldPassword.visibility = View.VISIBLE
        binding.textInputLayout2.visibility = View.GONE
        binding.newPassword.visibility = View.GONE
        binding.textInputLayout3.visibility = View.GONE
        binding.newPasswordConfirm.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.newEmailEditText.visibility = View.GONE
        binding.updateButton2.visibility = View.GONE
    }

    fun cont(view: View){
        if(!TextUtils.isEmpty(binding.oldPassword.text.toString())){
            reauthenticatePass()
        } else {
            Toast.makeText(this, "Introduce correctamente tu contraseña", Toast.LENGTH_SHORT).show()
        }
    }



    private fun reauthenticatePass(){
        binding.newEmailEditText.visibility = View.GONE
        binding.updateButton2.visibility = View.GONE

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.let {
            val credential = EmailAuthProvider.getCredential(it.email!!, binding.oldPassword.text.toString())
            it.reauthenticate(credential).addOnCompleteListener {
                if(it.isSuccessful){
                    binding.textInputLayout2.visibility = View.VISIBLE
                    binding.newPassword.visibility = View.VISIBLE
                    binding.updateButton.visibility = View.VISIBLE
                    binding.textInputLayout3.visibility = View.VISIBLE
                    binding.newPasswordConfirm.visibility = View.VISIBLE
                } else{
                    Toast.makeText(this, "Introduce correctamente tu contraseña", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun reauthenticateEmail(){
        binding.textInputLayout2.visibility = View.GONE
        binding.newPassword.visibility = View.GONE
        binding.textInputLayout3.visibility = View.GONE
        binding.newPasswordConfirm.visibility = View.GONE
        binding.updateButton.visibility = View.GONE

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.let {
            val credential = EmailAuthProvider.getCredential(it.email!!, binding.oldPassword.text.toString())
            it.reauthenticate(credential).addOnCompleteListener {
                if(it.isSuccessful){
                    binding.newEmailEditText.visibility = View.VISIBLE
                    binding.updateButton2.visibility = View.VISIBLE
                } else{
                    Toast.makeText(this, "Introduce correctamente tu contraseña", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updatePass(view: View){
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(binding.newPassword.text.toString() == binding.newPasswordConfirm.text.toString()){
            user?.let{
                user.updatePassword(binding.newPassword.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        } else {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show()
        }
    }

    fun updateEmail(view: View){
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        db.collection("users")
            .document(binding.emailEditText.text.toString())
            .update(
                binding.emailEditText.text.toString(), binding.newEmailEditText.text.toString()
            )

        user?.let{
            user.updateEmail(binding.newEmailEditText.text.toString())
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email actualizado", Toast.LENGTH_LONG).show()
                        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                finish()
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                    }
                }
        }
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