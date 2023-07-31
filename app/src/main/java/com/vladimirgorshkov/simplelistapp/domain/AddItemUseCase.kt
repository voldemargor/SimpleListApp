package com.vladimirgorshkov.simplelistapp.domain

import javax.inject.Inject

class AddItemUseCase @Inject constructor(private val repository: Repository) {

    suspend fun addItem(item: Item) {
        repository.addItem(item)
    }

}