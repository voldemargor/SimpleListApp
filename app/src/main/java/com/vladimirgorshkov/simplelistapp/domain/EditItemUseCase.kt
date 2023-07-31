package com.vladimirgorshkov.simplelistapp.domain

import javax.inject.Inject

class EditItemUseCase @Inject constructor(private val repository: Repository) {

    suspend fun editItem(item: Item) {
        repository.editItem(item)
    }

}