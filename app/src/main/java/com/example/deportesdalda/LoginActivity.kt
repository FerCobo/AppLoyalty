package com.example.deportesdalda

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.deportesdalda.DeportesDaldaApplication.Companion.prefs
import com.example.deportesdalda.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import android.os.Handler
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private val MY_PREFS = "SharedPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        initControls()
    }

    fun forgotPassword(view: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    fun register2(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun login(view: View) {
        loginUser()
    }

    fun google(view: View) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.deportesdalda.es/")))
    }

    fun instagram(view: View) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.instagram.com/daldadeportes/")
            )
        )
    }

    fun whatsapp(view: View) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+34697209249/")))
    }

    private fun initControls() {
        val prefs = getSharedPreferences(MY_PREFS, 0)
        val thisEmail = prefs.getString("email", "")
        val thisPassword = prefs.getString("password", "")
        val thisRemember = prefs.getBoolean("remember", false)
        if (thisRemember) {
            binding.emailEditText.setText(thisEmail)
            binding.passwordEditText.setText(thisPassword)
            binding.rememberMe.setChecked(thisRemember)
        }

        binding.rememberMe.setOnClickListener() {
            remember()
        }
    }

    private fun remember() {
        val thisRemember: Boolean = binding.rememberMe.isChecked
        val prefs = getSharedPreferences(MY_PREFS, 0)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("remember", thisRemember)
        editor.apply()


        val myEditor: SharedPreferences.Editor = prefs.edit()
        myEditor.putString("email", binding.emailEditText.text.toString())
        myEditor.putString("password", binding.passwordEditText.text.toString())
        val rememberThis: Boolean = binding.rememberMe.isChecked
        myEditor.putBoolean("rememberThis", rememberThis)
        myEditor.apply()
    }

    private fun loginUser() {
        val user = auth.currentUser
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (email == "deportesdalda@gmail.com" && password == "daldadelicias") {
            binding.progressBar.visibility = View.VISIBLE
            administrator()
            binding.progressBar.visibility = View.GONE
        } else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            prefs.saveEmail(email)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (user != null) {
                            if (user.isEmailVerified) {
                                binding.progressBar.visibility = View.VISIBLE
                                action()
                                binding.progressBar.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this,
                                    "Debes verificar tu correo",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "No existe una cuenta asociada a ese correo",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Debes introducir un correo y contraseña válidos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun administrator() {
        val intent = Intent(this, AdministratorActivity::class.java)
        startActivity(intent)
    }

    private fun action() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", binding.emailEditText.text.toString())
        startActivity(intent)
    }

    private lateinit var timer: Timer
    private val noDelay = 0L
    private val everyFiveSeconds = 28000L

    override fun onResume() {
        super.onResume()

        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    randomImages()
                }
            }
        }

        timer = Timer()
        timer.schedule(timerTask, noDelay, everyFiveSeconds)
    }

    override fun onPause() {
        super.onPause()

        timer.cancel()
        timer.purge()
    }

    private fun randomImages(){
        binding.advert2.visibility = View.GONE
        binding.advert3.visibility = View.GONE
        binding.advert4.visibility = View.GONE
        val image = FirebaseStorage.getInstance().reference.child("anuncios/Image1.JPG")
        val localFile = File.createTempFile("tempImage", ".jpg")
        image.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.advert.setImageBitmap(bitmap)
        }

        Handler().postDelayed(Runnable {
            binding.advert.visibility = View.GONE
            binding.advert2.visibility = View.VISIBLE
            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image2.JPG")
            val localFile = File.createTempFile("tempImage", ".jpg")
            image.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.advert2.setImageBitmap(bitmap)
                Handler().postDelayed(Runnable {
                    binding.advert2.visibility = View.GONE
                    binding.advert3.visibility = View.VISIBLE
                    val image = FirebaseStorage.getInstance().reference.child("anuncios/Image3.JPG")
                    val localFile = File.createTempFile("tempImage", ".jpg")
                    image.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.advert3.setImageBitmap(bitmap)
                        Handler().postDelayed(Runnable {
                            binding.advert3.visibility = View.GONE
                            binding.advert4.visibility = View.VISIBLE
                            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image4.JPG")
                            val localFile = File.createTempFile("tempImage", ".jpg")
                            image.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.advert4.setImageBitmap(bitmap)
                            }
                        }, 7000)
                    }
                }, 7000)
            }
        }, 7000)

    }
}