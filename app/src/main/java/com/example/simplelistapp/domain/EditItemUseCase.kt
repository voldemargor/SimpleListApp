package com.example.simplelistapp.domain

class EditItemUseCase(private val repository: Repository) {

    fun editItem(item: Item) {
        repository.editItem(item)
    }

}