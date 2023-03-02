package com.example.simplelistapp.domain

class EditListCardUseCase(private val repository: Repository) {

    fun editListCard(newListCard: ListCard) {
        repository.editListCard(newListCard)
    }

}