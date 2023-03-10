package com.example.simplelistapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FoldersScreenViewModel
    private lateinit var rvAdapter: FoldersListAdapter
    private lateinit var rvFoldersList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(FoldersScreenViewModel::class.java)

        setupRecyclerView()

        viewModel.foldersList.observe(this) {
            rvAdapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        rvFoldersList = findViewById<RecyclerView>(R.id.rv_folders_list)
        rvAdapter = FoldersListAdapter()
        rvFoldersList.adapter = rvAdapter

        setupOnClickListener()
        setupOnLongClickListener()
        setupSwipeListener()
    }

    private fun setupOnLongClickListener() {
        rvAdapter.onFolderLongClickListener = {
            Toast.makeText(this@MainActivity, "Click: ${it.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupOnClickListener() {
        rvAdapter.onFolderClickListener = {
            val items = viewModel.getItemsForFolder(it.id).value
            Log.d("mylog", items.toString())
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
        itemTouchHelper.attachToRecyclerView(rvFoldersList)
    }


}