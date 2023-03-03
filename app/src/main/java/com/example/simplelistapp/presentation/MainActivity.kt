package com.example.simplelistapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FoldersScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(FoldersScreenViewModel::class.java)

        viewModel.foldersList.observe(this) {
            Log.d("mylog", it.toString())
        }


    }


}