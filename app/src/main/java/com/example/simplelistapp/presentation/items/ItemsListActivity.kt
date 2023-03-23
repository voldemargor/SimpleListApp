package com.example.simplelistapp.presentation.items

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.databinding.ActivityItemsListBinding
import com.example.simplelistapp.domain.Folder

class ItemsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemsListBinding
    private lateinit var viewModelFactory: ItemsViewModelFactory
    private lateinit var viewModel: ItemsListViewModel
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
        title = folderName

        viewModel.itemsList.observe(this) {
            rvAdapter.submitList(it)
        }

        binding.fabAddItem.setOnClickListener() {
            viewModel.addItem()
//            startActivity(EditItemActivity.newIntentAddFolder(this))
        }

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
        viewModelFactory = ItemsViewModelFactory(folderId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ItemsListViewModel::class.java)
    }

    private fun setupRecyclerView() {
        rvAdapter = ItemsListAdapter()
        binding.rvItemsList.adapter = rvAdapter

        setupOnClickListener()
        setupOnLongClickListener()
        setupSwipeListener()
    }

    private fun setupOnClickListener() {
        rvAdapter.onItemClickListener = {
            viewModel.changeEnabledState(it)
        }
    }

    private fun setupOnLongClickListener() {
        rvAdapter.onItemLongClickListener = {
            Toast.makeText(this, "Edit: ${it.id} - ${it.name}", Toast.LENGTH_SHORT).show()
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
                val item = rvAdapter.currentList.get(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvItemsList)
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