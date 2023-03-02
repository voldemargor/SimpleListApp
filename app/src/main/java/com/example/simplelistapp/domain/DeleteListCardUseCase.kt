package com.example.simplelistapp.domain

class DeleteListCardUseCase(private val repository: Repository) {

    fun deleteListCard(listCard: ListCard) {
        repository.deleteListCard(listCard)
    }

}