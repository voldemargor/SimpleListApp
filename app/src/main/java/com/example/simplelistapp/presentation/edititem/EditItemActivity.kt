package com.example.simplelistapp.presentation.edititem

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.R
import com.example.simplelistapp.databinding.ActivityEditItemBinding
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.domain.Item

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private lateinit var viewModel: EditItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var itemId = Item.UNDEFINED_ID
    private var folderId = Folder.UNDEFINED_ID
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        parseIntent()

        viewModel = ViewModelProvider(this).get(EditItemViewModel::class.java)

        observeCommonViewModel()
        addCommonListeners()

        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }

        binding.tilItemName.requestFocus()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE))
            throw RuntimeException("Param Screen mode is absent")

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown Screen mode: $mode")

        screenMode = mode
        when (screenMode) {
            MODE_EDIT -> {
                if (!intent.hasExtra(EXTRA_ITEM_ID))
                    throw RuntimeException("Param ITEM ID is absent")
                itemId = intent.getIntExtra(EXTRA_ITEM_ID, Item.UNDEFINED_ID)
            }
            MODE_ADD -> {
                if (!intent.hasExtra(EXTRA_FOLDER_ID))
                    throw RuntimeException("Param FOLDER ID is absent")
                folderId = intent.getIntExtra(EXTRA_FOLDER_ID, Folder.UNDEFINED_ID)
            }
        }
    }

    private fun launchAddMode() {
        binding.btnSave.text = getString(R.string.btn_add)
        binding.etCount.setText(null)
        binding.btnSave.setOnClickListener() {
            val name = binding.etName.text?.trim().toString()
            val count = Integer.parseInt(binding.etCount.text?.trim().toString())

            if (name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.addItem(folderId, name, count)
        }
    }

    private fun launchEditMode() {
        binding.btnSave.text = getString(R.string.btn_save)

        viewModel.getItem(itemId)
        viewModel.currentItem.observe(this) {
            binding.etName.setText(it.name)
            binding.etName.setSelection(binding.etName.length())
            count = it.count

            updateCountViews()
        }

        binding.btnSave.setOnClickListener() {
            val name = binding.etName.text?.trim().toString()
            if (name.isBlank())
                viewModel.displayErrorInputName()
            else {

                viewModel.editItem(name, count)
            }
        }
    }

    private fun observeCommonViewModel() {
        // Ошибка имени
        viewModel.errorInputName.observe(this) {
            binding.tilItemName.error = null
            if (it) binding.tilItemName.error = getString(R.string.error_item_name)
        }
        // Закрыть экран
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addCommonListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.btnCancel.setOnClickListener() {
            viewModel.exitScreen()
        }

        binding.btnAddCount.setOnClickListener() {
            count++
            updateCountViews()
        }

        binding.btnRemoveCount.setOnClickListener() {
            if (count > 0) {
                count--
                updateCountViews()
            }
        }
    }

    private fun updateCountViews() {
        if (count == 0) {
            binding.etCount.setText(null)
            binding.btnRemoveCount.setBackgroundResource(R.drawable.ic_baseline_remove_circle_disabled_24)
        } else {
            binding.etCount.setText(count.toString())
            binding.btnRemoveCount.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_ITEM_ID = "item_id"
        private const val EXTRA_FOLDER_ID = "folder_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context, folderId: Int): Intent {
            return Intent(context, EditItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
                putExtra(EXTRA_FOLDER_ID, folderId)
            }
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            return Intent(context, EditItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_ITEM_ID, itemId)
            }
        }
    }

}