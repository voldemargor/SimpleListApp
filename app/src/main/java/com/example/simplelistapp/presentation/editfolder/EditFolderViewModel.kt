package com.example.simplelistapp.presentation.editfolder

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.AddFolderUseCase
import com.example.simplelistapp.domain.EditFolderUseCase
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.domain.GetFolderUseCase
import kotlinx.coroutines.launch

class EditFolderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)
    private val getFolderUseCase = GetFolderUseCase(repository)
    private val addFolderUseCase = AddFolderUseCase(repository)
    private val editFolderUseCase = EditFolderUseCase(repository)

    // Ошибка ввода названия
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    // Выбранный Folder
    private val _currentFolder = MutableLiveData<Folder>()
    val currentFolder: LiveData<Folder>
        get() = _currentFolder

    // Закрыть экран
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getFolder(id: Int) {
        viewModelScope.launch {
            _currentFolder.value = getFolderUseCase.getFolder(id)
        }
    }

    fun addFolder(name: String?) {
        Log.d("mylog", "Вызов viewModel.addFolder()")
        // TODO На самом деле если ничего не ввели, то нужно просто создавать с дефолтным именем "Новый список"
        val folderName = parseName(name)
        val inputsValid = validateInput(folderName)
        if (inputsValid) {
            viewModelScope.launch {
                val newFolder = Folder(parseName(folderName))
                addFolderUseCase.addFolder(newFolder)
                exitScreen()
            }
        }
    }

    fun editFolder(name: String) {
        val folderName = parseName(name)
        val inputsValid = validateInput(folderName)
        if (inputsValid) {
            _currentFolder.value?.let {
                viewModelScope.launch {
                    // id объекта сохраняется, так как делаем копию
                    val newFolder = it.copy(name = folderName)
                    editFolderUseCase.editFolder(newFolder)
                    exitScreen()
                }
            }
        }
    }

    fun displayErrorInputName() {
        _errorInputName.value = true
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun exitScreen() {
        _shouldCloseScreen.value = Unit
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String): Boolean {
        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }
        return true
    }
}