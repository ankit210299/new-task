package com.example.realmtaskapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realmtaskapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var persons: MutableList<ModelDetails>

    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        database = FirebaseDatabase.getInstance().reference.child("modelDetails")
        persons = mutableListOf()
        adapter = MyAdapter(persons, object : MyAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(view: View, position: Int) {
                performOptionsMenuClick(view, position)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        addDummyData()
        fetchPersons()
    }


    private fun addDummyData() {
        val persons = listOf(
            ModelDetails(name = "John", age = 30, city = "New York"),
            ModelDetails(name = "Alice", age = 39, city = "Los Angeles"),
            ModelDetails(name = "Mick", age = 25, city = "WashingTon"),
            ModelDetails(name = "Happy", age = 21, city = "England"),
            ModelDetails(name = "Sam", age = 24, city = "America"),
            ModelDetails(name = "Harry", age = 29, city = "United Kingdom"),
            ModelDetails(name = "Henry", age = 35, city = "France"),
            ModelDetails(name = "Marvel", age = 34, city = "UK")
        )

        for (person in persons) {
            database.push().setValue(person)
        }
    }

    private fun fetchPersons() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                persons.clear()
                for (childSnapshot in snapshot.children) {
                    val name = childSnapshot.child("name").getValue(String::class.java)
                    val age = childSnapshot.child("age").getValue(Int::class.java)
                    val city = childSnapshot.child("city").getValue(String::class.java)
                    if (name != null && age != null && city != null) {
                        persons.add(ModelDetails(name, age, city))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
               Log.e("Error", " $error")
            }
        })
    }

    private fun performOptionsMenuClick(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.menu_sort_name -> {
                        sortBy("name")
                        return true
                    }

                    R.id.menu_sort_city -> {
                        sortBy("city")
                        return true
                    }

                    R.id.menu_sort_age -> {
                        sortBy("age")
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    private fun sortBy(field: String) {
        when (field) {
            "name" -> {
                persons.sortBy { it.name }
                adapter.notifyDataSetChanged()
            }

            "age" -> {
                persons.sortBy { it.age }
                adapter.notifyDataSetChanged()
            }

            "city" -> {
                persons.sortBy { it.city }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}