package com.example.simplelistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.R
import com.example.simplelistapp.domain.Folder
import com.google.android.material.textfield.TextInputLayout

class EditFolderActivity : AppCompatActivity() {

    private lateinit var viewModel: EditFolderViewModel
    private var screenMode = MODE_UNKNOWN
    private var folderId = Folder.UNDEFINED_ID

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var currentFolder: Folder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_folder)

        parseIntent()

        viewModel = ViewModelProvider(this).get(EditFolderViewModel::class.java)
        initViews()

        observeCommonViewModel()
        addCommonListeners()

        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }

        tilName.requestFocus()
    }

    private fun launchAddMode() {
        btnSave.text = getString(R.string.btn_create)
        btnSave.setOnClickListener() {
            val name = etName.text.trim().toString()

            // TODO так надо будет в итоге, а пока покажу ошибку
             // if (name.isBlank()) name = getString(R.string.defaul_folder_name)

            if (name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.addFolder(name)
        }
    }

    private fun launchEditMode() {
        btnSave.text = getString(R.string.btn_save)

        viewModel.getFolder(folderId)
        viewModel.currentFolder.observe(this) {
            currentFolder = it
            etName.setText(it.name)
            etName.setSelection(etName.length())
        }

        btnSave.setOnClickListener() {
            val name = etName.text.trim().toString()
            if (name.isBlank())
                viewModel.displayErrorInputName()
            else
                viewModel.editFolder(name)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.text_input_layout_name)
        etName = findViewById(R.id.edit_text_name)
        btnSave = findViewById(R.id.button_save)
        btnCancel = findViewById(R.id.button_cancel)
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
            tilName.error = null
            if (it) tilName.error = getString(R.string.error_folder_name)
        }
        // Закрыть экран
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addCommonListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        btnCancel.setOnClickListener() {
            viewModel.exitScreen()
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_FOLDER_ID = "folder_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddFolder(context: Context): Intent {
            return Intent(context, EditFolderActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }
        }

        fun newIntentEditFolder(context: Context, folderId: Int): Intent {
            return Intent(context, EditFolderActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_FOLDER_ID, folderId)
            }
        }
    }

}