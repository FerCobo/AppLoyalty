package com.example.deportesdalda

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityReservationBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class ReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Reservas")

        initialise()
        table()
    }

    private fun initialise(){
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("reservations")
    }

    fun add(view: View) {
        if (!TextUtils.isEmpty(binding.nameTxt.text.toString()) && !TextUtils.isEmpty(binding.phoneTxt.text.toString())
            && !TextUtils.isEmpty(binding.dateTxt.text.toString())) {
            if (binding.phoneTxt.text.toString().length < 9 || binding.phoneTxt.text.toString().length > 9) {
                Toast.makeText(this, "El teléfono introducido no es válido", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("reservations")
                    .document(binding.phoneTxt.text.toString())
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            db.collection("reservations")
                                .document(binding.phoneTxt.text.toString())
                                .set(
                                    hashMapOf(
                                        "name" to binding.nameTxt.text.toString(),
                                        "date" to binding.dateTxt.text.toString()
                                    ), SetOptions.merge()
                                )
                        } else {
                            db.collection("reservations")
                                .document(binding.phoneTxt.text.toString())
                                .set(
                                    hashMapOf(
                                        "name" to binding.nameTxt.text.toString(),
                                        "phone" to binding.phoneTxt.text.toString(),
                                        "date" to binding.dateTxt.text.toString()
                                    )
                                )
                                .addOnSuccessListener {
                                    binding.nameTxt.setText("")
                                    binding.phoneTxt.setText("")
                                    binding.dateTxt.setText("")
                                    Toast.makeText(this, "Reserva registrada", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
            }
        } else{
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    fun delete(view: View) {
        if (TextUtils.isEmpty(binding.phoneTxt.text.toString())) {
            Toast.makeText(this, "Debes introducir un teléfono", Toast.LENGTH_SHORT).show()
        } else {
            db.collection("reservations")
                .document(binding.phoneTxt.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        Toast.makeText(this, "No existe una reserva asociada a ese teléfono", Toast.LENGTH_SHORT).show()
                    } else {
                        val alertDialog = AlertDialog.Builder(this)

                        alertDialog.apply {
                            setTitle("Eliminar reserva")
                            setMessage("¿Estás seguro/a de que deseas eliminar esta reserva?")
                            setPositiveButton(
                                "Sí",
                                DialogInterface.OnClickListener { dialog, id -> yes() })
                            setNegativeButton("No", null)
                        }.create().show()
                    }
                }
        }
    }

    private fun yes() {
        db.collection("reservations").document(binding.phoneTxt.text.toString())
            .delete()
            .addOnSuccessListener {
                binding.phoneTxt.setText("")
                Toast.makeText(this, "Reserva eliminada", Toast.LENGTH_SHORT).show()
            }
    }

    private fun table(){
        db.collection("reservations").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                var data1 = ""
                var data2 = ""
                var data3 = ""
                for (documentSnapshot in queryDocumentSnapshots) {
                    val users: Users = documentSnapshot.toObject(Users::class.java)

                    users.setName(documentSnapshot.getString("name"))
                    val name: String? = users.getName()

                    users.setPhone(documentSnapshot.id)
                    val phone: String? = users.getPhone()

                    users.setDate(documentSnapshot.getString("date"))
                    val date: String? = users.getDate()

                    data1 += "$name\n"
                    data2 += "$phone\n"
                    data3 += "$date\n"
                }
                binding.txtName.text = data1
                binding.txtPhone.text = data2
                binding.txtDate.text = data3
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu6, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            R.id.nav_refresh -> {
                finish()
                startActivity(Intent(this, ReservationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}