package com.vladimirgorshkov.simplelistapp.presentation.edititem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimirgorshkov.simplelistapp.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor() : ViewModel() {

    //private val repository = DbRepositoryImpl(application)
    @Inject lateinit var getItemUseCase: GetItemUseCase
    @Inject lateinit var addItemUseCase: AddItemUseCase
    @Inject lateinit var editItemUseCase: EditItemUseCase
    @Inject lateinit var getFolderUseCase: GetFolderUseCase
    @Inject lateinit var editFolderUseCase: EditFolderUseCase

    // Ошибка ввода названия
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> get() = _errorInputName

    // Выбранный Item
    private val _currentItem = MutableLiveData<Item>()
    val currentItem: LiveData<Item> get() = _currentItem

    // Закрыть экран
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> get() = _shouldCloseScreen

    fun getItem(id: Int) {
        viewModelScope.launch {
            _currentItem.value = getItemUseCase.getItem(id)
        }
    }

    fun addItem(folderId: Int, name: String, count: Int) {
        val itemName = parseName(name)
        val inputsValid = validateInput(itemName)
        if (inputsValid) {
            viewModelScope.launch {
                val newItem = Item(folderId, parseName(itemName), count)
                incrementItemsCountInFolder(folderId)
                addItemUseCase.addItem(newItem)
                exitScreen()
            }
        }
    }

    fun editItem(name: String, count: Int) {
        val itemName = parseName(name)
        val inputsValid = validateInput(itemName)
        if (inputsValid) {
            _currentItem.value?.let {
                viewModelScope.launch {
                    // id объекта сохраняется, так как делаем копию
                    val newItem = it.copy(name = itemName, count = count)
                    editItemUseCase.editItem(newItem)
                    exitScreen()
                }
            }
        }
    }

    private suspend fun incrementItemsCountInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        val newFolder = folder.copy(itemsCount = folder.itemsCount.inc())
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