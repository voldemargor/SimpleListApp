package com.example.simplelistapp.presentation

import androidx.lifecycle.ViewModel
import com.example.simplelistapp.data.TempRepositoryImpl
import com.example.simplelistapp.domain.*

class FoldersScreenViewModel : ViewModel() {

    private val repository = TempRepositoryImpl

    private val getFoldersListUseCase = GetFoldersListUseCase(repository)
    private val editFolderUseCase = EditFolderUseCase(repository)
    private val deleteFolderUseCase = DeleteFolderUseCase(repository)

    val foldersList = getFoldersListUseCase.getFoldersList()

    fun editFolder(folder: ItemsFolder) {
        editFolderUseCase.editFolder(folder)
    }

    fun deleteFolder(folder: ItemsFolder) {
        deleteFolderUseCase.deleteFolder(folder)
    }

}