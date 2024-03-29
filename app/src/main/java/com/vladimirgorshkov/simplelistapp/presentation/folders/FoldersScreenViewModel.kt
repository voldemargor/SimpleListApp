package com.vladimirgorshkov.simplelistapp.presentation.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimirgorshkov.simplelistapp.domain.DeleteFolderUseCase
import com.vladimirgorshkov.simplelistapp.domain.Folder
import com.vladimirgorshkov.simplelistapp.domain.GetFoldersListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersScreenViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var getFoldersListUseCase: GetFoldersListUseCase
    @Inject lateinit var deleteFolderUseCase: DeleteFolderUseCase

    lateinit var foldersList: LiveData<List<Folder>>

    fun initFoldersLD() {
        viewModelScope.launch {
            foldersList = getFoldersListUseCase.getFoldersList()
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.deleteFolder(folder)
        }
    }


}