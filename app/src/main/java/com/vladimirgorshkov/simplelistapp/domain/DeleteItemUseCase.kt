package com.vladimirgorshkov.simplelistapp.domain

import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(private val repository: Repository) {

    suspend fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }

}