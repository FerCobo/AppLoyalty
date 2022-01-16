package com.example.deportesdalda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityAdministratorBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class AdministratorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityAdministratorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Administrador")
    }

    fun clean(view: View){
        binding.emailEditText.setText("")
        binding.textPoints.setText("")
        binding.nameText.setText("")
        binding.lastNameText.setText("")
    }

    fun update(view: View){
        if (!TextUtils.isEmpty(binding.emailEditText.text.toString())) {
            db.collection("users")
                .document(binding.emailEditText.text.toString())
                .update(mapOf(
                    "puntos" to binding.textPoints.text.toString()
                ))
                .addOnSuccessListener { Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show() }
        } else {
            Toast.makeText(this, "No se ha detectado ningún correo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initScanner(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                binding.emailEditText.setText(result.contents)
                action()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun action(){
        db.collection("users")
            .document(binding.emailEditText.text.toString())
            .get().addOnSuccessListener {
                binding.textPoints.setText(it.get("puntos") as String?)
                binding.nameText.setText(it.get("nombre") as String?)
                binding.lastNameText.setText(it.get("apellidos") as String?)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu5, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            R.id.nav_qr -> {
                initScanner()
                true
            }
            R.id.nav_edit -> {
                startActivity(Intent(this, EditAdminActivity::class.java))
                true
            }
            R.id.nav_list -> {
                startActivity(Intent(this, ListUsersActivity::class.java))
                true
            }
            R.id.nav_advert -> {
                startActivity(Intent(this, AdvertAdminActivity::class.java))
                true
            }
            R.id.nav_reservation-> {
                startActivity(Intent(this, ReservationActivity::class.java))
                true
            }
            R.id.nav_shop-> {
                startActivity(Intent(this, ShopAdminActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}