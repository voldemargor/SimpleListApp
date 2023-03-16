package com.example.simplelistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplelistapp.data.TempRepositoryImpl
import com.example.simplelistapp.domain.*

class FoldersScreenViewModel : ViewModel() {

    private val repository = TempRepositoryImpl

    private val getFoldersListUseCase = GetFoldersListUseCase(repository)
    private val addFolderUseCase = AddFolderUseCase(repository)
    private val editFolderUseCase = EditFolderUseCase(repository)
    private val deleteFolderUseCase = DeleteFolderUseCase(repository)
    private val getItemsForFolderUseCase = GetItemsForFolderUseCase(repository)

    val foldersList = getFoldersListUseCase.getFoldersList()

    fun addFolder(folder: Folder) {
        addFolderUseCase.addFolder(folder)
    }

    fun editFolder(folder: Folder) {
        editFolderUseCase.editFolder(folder)
    }

    fun deleteFolder(folder: Folder) {
        deleteFolderUseCase.deleteFolder(folder)
    }

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>> {
        return getItemsForFolderUseCase.getItemsForFolder(folderId)
    }

}