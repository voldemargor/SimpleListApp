package com.vladimirgorshkov.simplelistapp.domain

import javax.inject.Inject

class GetItemUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getItem(itemId: Int): Item {
        return repository.getItem(itemId)
    }

}