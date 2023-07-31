package com.vladimirgorshkov.simplelistapp.presentation.edititem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vladimirgorshkov.simplelistapp.R
import com.vladimirgorshkov.simplelistapp.databinding.ActivityEditItemBinding
import com.vladimirgorshkov.simplelistapp.domain.Folder
import com.vladimirgorshkov.simplelistapp.domain.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private val viewModel by lazy { ViewModelProvider(this).get(EditItemViewModel::class.java) }

    private var screenMode = MODE_UNKNOWN
    private var itemId = Item.UNDEFINED_ID
    private var folderId = Folder.UNDEFINED_ID
    private var countInputted = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater).also { setContentView(it.root) }

        supportActionBar?.hide()

        parseIntent()

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
            val input = getFieldsInput()
            if (input.name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.addItem(folderId, input.name, input.count)
        }
    }

    private fun launchEditMode() {
        binding.btnSave.text = getString(R.string.btn_save)

        viewModel.getItem(itemId)
        viewModel.currentItem.observe(this) {
            binding.etName.setText(it.name)
            binding.etName.setSelection(binding.etName.length())
            binding.etCount.setText(it.count.toString())
            countInputted = it.count

            refreshCountView()
        }

        binding.btnSave.setOnClickListener() {
            val input = getFieldsInput()
            if (input.name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.editItem(input.name, input.count)
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

        val nameFieldListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.etName.addTextChangedListener(nameFieldListener)

        val countFieldListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                countInputted = parseCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.etCount.addTextChangedListener(countFieldListener)

        binding.btnCancel.setOnClickListener() {
            viewModel.exitScreen()
        }

        binding.btnAddCount.setOnClickListener() {
            countInputted++
            refreshCountView()
            binding.etCount.setSelection(binding.etCount.length())
        }

        binding.btnRemoveCount.setOnClickListener() {
            if (countInputted > 0) {
                countInputted--
                refreshCountView()
                binding.etCount.setSelection(binding.etCount.length())
            }
        }
    }

    private fun refreshCountView() {
        if (countInputted == 0) {
            binding.etCount.setText(null)
            binding.btnRemoveCount.setBackgroundResource(R.drawable.ic_baseline_remove_circle_disabled_24)
        } else {
            binding.etCount.setText(countInputted.toString())
            binding.btnRemoveCount.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24)
        }
    }

    private fun getFieldsInput(): FieldsInput {
        val name = binding.etName.text?.trim().toString()
        return FieldsInput(name, parseCount())
    }

    private fun parseCount(): Int {
        var count = 0
        val countExtracted = binding.etCount.text?.filter { it.isDigit() } ?: ""
        if (countExtracted.isNotEmpty()) {
            count += abs(countExtracted.toString().toInt())
            count.coerceIn(0, 999)
        }
        return count
    }

    inner class FieldsInput(val name: String, val count: Int)

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