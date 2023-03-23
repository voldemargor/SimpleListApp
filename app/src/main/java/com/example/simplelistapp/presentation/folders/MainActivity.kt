package com.example.simplelistapp.presentation.folders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.databinding.ActivityMainBinding
import com.example.simplelistapp.presentation.editfolder.EditFolderActivity
import com.example.simplelistapp.presentation.items.ItemsListActivity

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
        observeViewModel()
        setFabListener()
    }

    private fun setupRecyclerView() {
        rvAdapter = FoldersListAdapter()
        binding.rvFoldersList.adapter = rvAdapter

        setOnClickListener()
        setOnLongClickListener()
        setSwipeListener()
    }

    private fun observeViewModel() {
        viewModel.foldersList.observe(this) {
            rvAdapter.submitList(it)
        }
    }

    private fun setFabListener() {
        binding.fabAddFolder.setOnClickListener() {
            startActivity(EditFolderActivity.newIntentAddFolder(this))
        }
    }

    private fun setOnClickListener() {
        rvAdapter.onFolderClickListener = {
            startActivity(ItemsListActivity.newIntentItemsList(this, it.id, it.name))
        }
    }

    private fun setOnLongClickListener() {
        rvAdapter.onFolderLongClickListener = {
            startActivity(EditFolderActivity.newIntentEditFolder(this, it.id))
        }
    }

    private fun setSwipeListener() {
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