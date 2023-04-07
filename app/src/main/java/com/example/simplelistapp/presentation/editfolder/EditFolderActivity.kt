package com.example.simplelistapp.presentation.editfolder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.R
import com.example.simplelistapp.databinding.ActivityEditFolderBinding
import com.example.simplelistapp.domain.Folder

class EditFolderActivity : AppCompatActivity() {

    private lateinit var viewModel: EditFolderViewModel
    private var screenMode = MODE_UNKNOWN
    private var folderId = Folder.UNDEFINED_ID

    private lateinit var binding: ActivityEditFolderBinding

    private var currentFolder: Folder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        parseIntent()

        viewModel = ViewModelProvider(this).get(EditFolderViewModel::class.java)

        observeCommonViewModel()
        addCommonListeners()

        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }

        binding.tilName.requestFocus()

    }

    private fun launchAddMode() {
        binding.btnSave.text = getString(R.string.btn_create)
        binding.btnSave.setOnClickListener() {
            var name = binding.etName.text?.trim().toString()

            if (name.isBlank()) name = getString(R.string.default_folder_name)
            viewModel.addFolder(name)

            //if (name.isBlank())
            //    viewModel.displayErrorInputName()
            //else
            //    viewModel.addFolder(name)
        }
    }

    private fun launchEditMode() {
        binding.btnSave.text = getString(R.string.btn_save)

        viewModel.getFolder(folderId)
        viewModel.currentFolder.observe(this) {
            currentFolder = it
            binding.etName.setText(it.name)
            binding.etName.setSelection(binding.etName.length())
        }

        binding.btnSave.setOnClickListener() {
            val name = binding.etName.text?.trim().toString()
            if (name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.editFolder(name)
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE))
            throw RuntimeException("Param Screen mode is absent")

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown Screen mode: $mode")

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_FOLDER_ID))
                throw RuntimeException("Param Folder ID is absent")
            folderId = intent.getIntExtra(EXTRA_FOLDER_ID, Folder.UNDEFINED_ID)
        }
    }

    private fun observeCommonViewModel() {
        // Ошибка имени
        viewModel.errorInputName.observe(this) {
            binding.tilName.error = null
            if (it) binding.tilName.error = getString(R.string.error_folder_name)
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
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_FOLDER_ID = "folder_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddFolder(context: Context) =
            Intent(context, EditFolderActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }

        fun newIntentEditFolder(context: Context, folderId: Int) =
            Intent(context, EditFolderActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_FOLDER_ID, folderId)
            }
    }

}