package com.example.simplelistapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FoldersScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)

    private val getFoldersListUseCase = GetFoldersListUseCase(repository)
    private val deleteFolderUseCase = DeleteFolderUseCase(repository)
    private val getItemsForFolderUseCase = GetItemsForFolderUseCase(repository)

    val foldersList = getFoldersListUseCase.getFoldersList()

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.deleteFolder(folder)
        }
    }

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>> {
        return getItemsForFolderUseCase.getItemsForFolder(folderId)
    }
}