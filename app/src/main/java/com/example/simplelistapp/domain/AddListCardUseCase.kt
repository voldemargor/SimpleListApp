package com.example.simplelistapp.domain

class AddListCardUseCase(private val repository: Repository) {

    fun addListCard(listCard: ListCard) {
        repository.addListCard(listCard)
    }

}