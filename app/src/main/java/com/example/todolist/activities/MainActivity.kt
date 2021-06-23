 package com.example.todolist.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todolist.R
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
 class MainActivity : AppCompatActivity() {
     private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navhostfragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

    }

     override fun onSupportNavigateUp(): Boolean {
         return navController.navigateUp()|| super.onSupportNavigateUp()
     }

}

 //Constants for navigate between fragments

 const val  ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
 const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1