package com.example.simplelistapp.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R
import com.example.simplelistapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FoldersScreenViewModel
    private lateinit var rvAdapter: FoldersListAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FoldersScreenViewModel::class.java)

        setupRecyclerView()

        viewModel.foldersList.observe(this) {
            rvAdapter.submitList(it)
        }

        findViewById<FloatingActionButton>(R.id.fab_add_folder).setOnClickListener() {
            startActivity(EditFolderActivity.newIntentAddFolder(this))
        }

    }

    private fun setupRecyclerView() {
        rvAdapter = FoldersListAdapter()
        binding.rvFoldersList.adapter = rvAdapter

        setupOnClickListener()
        setupOnLongClickListener()
        setupSwipeListener()
    }

    private fun setupOnClickListener() {
        rvAdapter.onFolderClickListener = {
            val items = viewModel.getItemsForFolder(it.id).value
            Log.d("mylog", "Folder ID: ${it.id}")
            Log.d("mylog", items.toString())
        }
    }

    private fun setupOnLongClickListener() {
        rvAdapter.onFolderLongClickListener = {
            Toast.makeText(this@MainActivity, "Click: ${it.name}", Toast.LENGTH_SHORT).show()
            startActivity(EditFolderActivity.newIntentEditFolder(this, it.id))
        }
    }

    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val folder = rvAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteFolder(folder)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvFoldersList)
    }


}