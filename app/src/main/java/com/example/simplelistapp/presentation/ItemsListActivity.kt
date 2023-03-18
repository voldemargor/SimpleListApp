package com.example.simplelistapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplelistapp.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}