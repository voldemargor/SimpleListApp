package com.vladimirgorshkov.simplelistapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    val name: String,
    val itemsCompleted: Int = 0,
    val itemsCount: Int = 0,
    var id: Int = UNDEFINED_ID
) : Parcelable {

    companion object {
        const val UNDEFINED_ID = 0
    }

}