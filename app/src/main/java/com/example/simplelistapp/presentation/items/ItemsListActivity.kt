package com.example.simplelistapp.presentation.items

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.databinding.ActivityItemsListBinding
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.presentation.edititem.EditItemActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsListActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(ItemsListViewModel::class.java) }

    private lateinit var binding: ActivityItemsListBinding
    private lateinit var rvAdapter: ItemsListAdapter

    private var folderId = Folder.UNDEFINED_ID
    private lateinit var folderName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        initViewModel()
        setupRecyclerView()
        observeViewModel()
        setFabListener()

        title = folderName
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_FOLDER_ID))
            throw RuntimeException("Param FOLDER ID is absent")

        folderId = intent.getIntExtra(EXTRA_FOLDER_ID, Folder.UNDEFINED_ID)
        if (folderId == Folder.UNDEFINED_ID)
            throw RuntimeException("Param FOLDER ID is WRONG")

        folderName = intent.getStringExtra(EXTRA_FOLDER_NAME)
            ?: throw RuntimeException("Param FOLDER NAME is absent")
    }

    private fun initViewModel() {
        viewModel.initItemsLD(folderId)
    }

    private fun setupRecyclerView() {
        rvAdapter = ItemsListAdapter()
        binding.rvItemsList.adapter = rvAdapter

        setOnClickListener()
        setOnLongClickListener()
        setItemCheckboxListener()
        setLeftSwipeListener()
        setRightSwipeListener()
    }

    private fun observeViewModel() {
        viewModel.itemsList.observe(this) {
            rvAdapter.submitList(it)
            toggleEmptyState(it.isEmpty())
        }
    }

    private fun setFabListener() {
        binding.fabAddItem.setOnClickListener() {
            startActivity(EditItemActivity.newIntentAddItem(this, folderId))
        }
    }

    private fun setOnClickListener() {
        rvAdapter.onItemClickListener = {
            startActivity(EditItemActivity.newIntentEditItem(this, it.id))
        }
    }

    private fun setOnLongClickListener() {
        rvAdapter.onItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }

    private fun setItemCheckboxListener() {
        rvAdapter.onItemCheckboxListener = {
            viewModel.changeEnabledState(it)
        }
    }

    private fun setLeftSwipeListener() {
        val callback = object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = rvAdapter.currentList.get(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvItemsList)
    }

    private fun setRightSwipeListener() {
        val callback = object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = rvAdapter.currentList.get(viewHolder.adapterPosition)
                viewModel.changeEnabledState(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvItemsList)
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvItemsList.visibility = View.GONE
            binding.emptyItemsLayout.visibility = View.VISIBLE
        } else {
            binding.rvItemsList.visibility = View.VISIBLE
            binding.emptyItemsLayout.visibility = View.GONE
        }
    }

    companion object {
        private const val EXTRA_FOLDER_ID = "extra_folder_id"
        private const val EXTRA_FOLDER_NAME = "extra_folder_name"

        fun newIntentItemsList(context: Context, folderId: Int, folderName: String): Intent {
            return Intent(context, ItemsListActivity::class.java).apply {
                putExtra(EXTRA_FOLDER_ID, folderId)
                putExtra(EXTRA_FOLDER_NAME, folderName)
            }
        }
    }
}