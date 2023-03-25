package com.example.simplelistapp.presentation.folders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.*
import kotlinx.coroutines.launch

class FoldersScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)

    private val getFoldersListUseCase = GetFoldersListUseCase(repository)
    private val deleteFolderUseCase = DeleteFolderUseCase(repository)

    val foldersList = getFoldersListUseCase.getFoldersList()

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.deleteFolder(folder)
        }
    }

}