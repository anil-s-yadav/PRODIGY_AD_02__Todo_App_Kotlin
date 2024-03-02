package com.example.todoapp

import TaskAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var edittextTitle: EditText
    private lateinit var edittextDesc: EditText
    private lateinit var buttonAdd: Button
    private lateinit var FloatintBtn: FloatingActionButton
    private lateinit var cardViewAddItem: CardView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var tasksList: ArrayList<String>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edittextTitle = findViewById(R.id.editTextTask)
        edittextDesc = findViewById(R.id.editTextDesc)
        buttonAdd = findViewById(R.id.buttonAdd)
        FloatintBtn = findViewById(R.id.buttonfloat)
        cardViewAddItem = findViewById(R.id.card_add_item)
        recyclerView = findViewById(R.id.listViewTasks)

        //supportActionBar?.title = "Add new Task"

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)

        tasksList = ArrayList(sharedPreferences.getStringSet("tasks", HashSet()) ?: HashSet())

        adapter = TaskAdapter(tasksList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        cardViewAddItem.visibility = View.GONE

        FloatintBtn.setOnClickListener {
            if (cardViewAddItem.isVisible){
                cardViewAddItem.visibility = View.GONE
            }else
            cardViewAddItem.visibility = View.VISIBLE
        }

        buttonAdd.setOnClickListener {
            val taskTitle = edittextTitle.text.toString()
            val taskDisc = edittextDesc.text.toString()

            if (taskTitle.isNotEmpty()) {
                val task = "$taskTitle\n$taskDisc"
                tasksList.add(task)
                adapter.notifyDataSetChanged()
                edittextTitle.text.clear()
                edittextDesc.text.clear()
                cardViewAddItem.visibility = View.GONE
            } else {
                edittextTitle.setError("Title is required")
            }
        }

    }

    override fun onStop() {
        super.onStop()
        // Save tasks to SharedPreferences when the activity is stopped
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", HashSet(tasksList))
        editor.apply()
    }

    override fun onRestart() {
        super.onRestart()
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", HashSet(tasksList))
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", HashSet(tasksList))
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", HashSet(tasksList))
        editor.apply()
    }


}
