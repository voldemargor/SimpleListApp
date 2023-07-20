package com.example.simplelistapp.presentation.editfolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.domain.AddFolderUseCase
import com.example.simplelistapp.domain.EditFolderUseCase
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.domain.GetFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFolderViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var getFolderUseCase: GetFolderUseCase
    @Inject lateinit var addFolderUseCase: AddFolderUseCase
    @Inject lateinit var editFolderUseCase: EditFolderUseCase

    // Ошибка ввода названия
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> get() = _errorInputName

    // Выбранный Folder
    private val _currentFolder = MutableLiveData<Folder>()
    val currentFolder: LiveData<Folder> get() = _currentFolder

    // Закрыть экран
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> get() = _shouldCloseScreen

    // Перейти внутрь списка
    private val _shouldDisplayItemsListScreen = MutableLiveData<Folder>()
    val shouldDisplayItemsListScreen: LiveData<Folder> get() = _shouldDisplayItemsListScreen

    fun getFolder(id: Int) {
        viewModelScope.launch {
            _currentFolder.value = getFolderUseCase.getFolder(id)
        }
    }

    fun addFolder(name: String?) {
        val folderName = parseName(name)
        val inputsValid = validateInput(folderName)
        if (inputsValid) {
            viewModelScope.launch {
                val newFolder = Folder(parseName(folderName))
                val folderId = addFolderUseCase.addFolder(newFolder)
                //exitScreen()
                goToItemsListScreen(getFolderUseCase.getFolder(folderId.toInt()))
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

    private fun goToItemsListScreen(folder: Folder) {
        _shouldDisplayItemsListScreen.value = folder
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