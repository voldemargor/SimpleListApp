package com.example.simplelistapp.domain

class EditListItemUseCase(private val repository: Repository) {

    fun editListItem(newListItem: ListItem) {
        repository.editListItem(newListItem)
    }

}