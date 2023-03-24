package com.example.simplelistapp.domain

data class Folder(
    val name: String,
    val itemsCompleted: Int = 0,
    val itemsCount: Int = 0,
    var id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = 0
    }

}