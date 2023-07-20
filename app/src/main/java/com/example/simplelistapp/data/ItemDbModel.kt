package com.example.simplelistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val folderId: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
) {

}
