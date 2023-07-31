package com.vladimirgorshkov.simplelistapp.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetFoldersListUseCase @Inject constructor(private val repository: Repository) {

    fun getFoldersList(): LiveData<List<Folder>> {
        return repository.getFoldersList()
    }

}