package com.example.deportesdalda

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.deportesdalda.databinding.ActivityListUsersBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class ListUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListUsersBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Listado de clientes")
        table()
        recountUsers()
    }

    private fun table(){
        db.collection("users").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                var data1 = ""
                var data2 = ""
                var data3 = ""
                var data4 = ""
                for (documentSnapshot in queryDocumentSnapshots) {
                    val users: Users = documentSnapshot.toObject(Users::class.java)
                    users.setEmail(documentSnapshot.id)
                    val email: String? = users.getEmail()
                    users.setName(documentSnapshot.getString("nombre"))
                    val name: String? = users.getName()
                    users.setLastname(documentSnapshot.getString("apellidos"))
                    val lastname: String? = users.getLastname()
                    users.setPoints(documentSnapshot.getString("puntos"))
                    val points: String? = users.getPoints()
                    data1 += "$email\n"
                    data2 += "$name\n"
                    data3 += "$lastname\n"
                    data4 += "$points\n"
                }
                binding.txtEmail.text = data1
                binding.txtName.text = data2
                binding.txtLastName.text = data3
                binding.txtPoints.text = data4
            })
    }

    private fun recountUsers(){
        db.collection("users").get().addOnSuccessListener { result ->
            val recount = result.size()
            val text = "El número total de clientes es: $recount"
            binding.textRecount.text = text
        }
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