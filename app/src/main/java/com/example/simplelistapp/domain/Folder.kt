package com.example.simplelistapp.domain

import com.example.simplelistapp.R

data class Folder(
    val name: String,
    val itemsCount: Int = 0,
    var id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = 0
    }

}