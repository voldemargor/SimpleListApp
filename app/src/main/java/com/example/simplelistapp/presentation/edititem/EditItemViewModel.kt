package com.example.simplelistapp.presentation.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.*
import kotlinx.coroutines.launch

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)
    private val getItemUseCase = GetItemUseCase(repository)
    private val addItemUseCase = AddItemUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)
    private val getFolderUseCase = GetFolderUseCase(repository)
    private val editFolderUseCase = EditFolderUseCase(repository)

    // Ошибка ввода названия
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    // Выбранный Item
    private val _currentItem = MutableLiveData<Item>()
    val currentItem: LiveData<Item>
        get() = _currentItem

    // Закрыть экран
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getItem(id: Int) {
        viewModelScope.launch {
            _currentItem.value = getItemUseCase.getItem(id)
        }
    }

    fun addItem(name: String, folderId: Int) {
        val itemName = parseName(name)
        val inputsValid = validateInput(itemName)
        if (inputsValid) {
            viewModelScope.launch {
                val newItem = Item(folderId, parseName(itemName), 0)
                increaseItemsCountInFolder(folderId)
                addItemUseCase.addItem(newItem)
                exitScreen()
            }
        }
    }

    fun editItem(name: String) {
        val itemName = parseName(name)
        val inputsValid = validateInput(itemName)
        if (inputsValid) {
            _currentItem.value?.let {
                viewModelScope.launch {
                    // id объекта сохраняется, так как делаем копию
                    val newItem = it.copy(name = itemName)
                    editItemUseCase.editItem(newItem)
                    exitScreen()
                }
            }
        }
    }

    private suspend fun increaseItemsCountInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        var newItemsCount = folder.itemsCount
        newItemsCount++
        val newFolder = folder.copy(itemsCount = newItemsCount)
        editFolderUseCase.editFolder(newFolder)
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

    private fun parseName(inputName: String): String {
        return inputName.trim()
    }

    private fun validateInput(name: String): Boolean {
        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }
        return true
    }
}