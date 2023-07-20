package com.example.simplelistapp.presentation.folders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.DeleteFolderUseCase
import com.example.simplelistapp.domain.Folder
import kotlinx.coroutines.launch

class FolderOptionsDialogViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)

    private val deleteFolderUseCase = DeleteFolderUseCase(repository)

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.deleteFolder(folder)
        }
    }

}