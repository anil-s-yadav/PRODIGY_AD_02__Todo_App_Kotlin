package com.example.todoapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var edittextTitle : EditText
    private lateinit var edittextDesc : EditText
    private  lateinit var buttonAdd : Button
    private  lateinit var FloatintBtn : FloatingActionButton
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var listViewTasks: ListView
    private lateinit var tasksArray: ArrayList<String>
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edittextTitle = findViewById(R.id.editTextTask)
        edittextDesc = findViewById(R.id.editTextDesc)
        buttonAdd = findViewById(R.id.buttonAdd)
        FloatintBtn = findViewById(R.id.buttonfloat)
        listViewTasks = findViewById(R.id.listViewTasks)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)

        tasksArray = ArrayList(sharedPreferences.getStringSet("tasks", HashSet()) ?: HashSet())

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasksArray)
        listViewTasks.adapter = adapter

        FloatintBtn.setOnClickListener {
            edittextTitle.visibility = View.VISIBLE
            edittextDesc.visibility = View.VISIBLE
            buttonAdd.visibility = View.VISIBLE
        }
        buttonAdd.setOnClickListener {
            val task = edittextTitle.text.toString().trim()
            if (task.isNotEmpty()) {
                tasksArray.add(task)
                adapter.notifyDataSetChanged()
                edittextTitle.text.clear()
            }
        }


    }


    override fun onStop() {
        super.onStop()
        // Save tasks to SharedPreferences when the activity is stopped
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", HashSet(tasksArray))
        editor.apply()
    }
}