package com.example.simplelistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.databinding.ActivityItemsListBinding
import com.example.simplelistapp.domain.Folder

class ItemsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemsListBinding
    private lateinit var viewModelFactory: ItemsViewModelFactory
    private lateinit var viewModel: ItemsListViewModel

    private var folderId = Folder.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        initViewModel()

        viewModel.itemsList.observe(this) {
//            Log.d("mylog", "Folder id: $folderId")
//            Log.d("mylog", "items count: ${it.size}")
//            for (item in it)
//                Log.d("mylog", item.name)

            val string = StringBuilder()
            string.append("Folder id: $folderId\n")
            for (item in it) {
                string.append(item.name)
                string.append("\n")
            }
            binding.textView.text = string.toString()
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
    }

    private fun initViewModel() {
        viewModelFactory = ItemsViewModelFactory(folderId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ItemsListViewModel::class.java)
    }

    companion object {

        private const val EXTRA_FOLDER_ID = "extra_folder_id"

        fun newIntentItemsList(context: Context, folderId: Int): Intent {
            return Intent(context, ItemsListActivity::class.java).apply {
                putExtra(EXTRA_FOLDER_ID, folderId)
            }
        }
    }
}