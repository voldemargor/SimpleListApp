package com.example.simplelistapp.data

import com.example.simplelistapp.domain.ItemsFolder
import com.example.simplelistapp.domain.Item
import com.example.simplelistapp.domain.Repository

object TempRepositoryImpl : Repository {

    private val folders = mutableListOf<ItemsFolder>()

    private val items = mutableListOf<Item>()

    private var autoincrementFolderId = 0
    private var autoincrementItemsId = 0

    init {
//        TODO("Сгенерить фолдеры")
//
//        for (i in 0..9) {
//            items.add(Item("Name$i", i, true))
//        }
    }

    override fun addFolder(folder: ItemsFolder) {
        folder.id = autoincrementFolderId++
        folders.add(folder)
    }

    override fun getFolder(folderId: Int): ItemsFolder {
        return folders.find { it.id == folderId }
            ?: throw RuntimeException("Folder with id $folderId not found")
    }

    override fun editFolder(folder: ItemsFolder) {
        val oldFolder = getFolder(folder.id)
        folders.remove(oldFolder)
        folders.add(folder)
    }

    override fun deleteFolder(folder: ItemsFolder) {
        folders.remove(folder)
    }

    override fun getFoldersList(): List<ItemsFolder> {
        return folders.toList() // возвращаем копию
    }

    override fun addItem(item: Item) {
        item.id = autoincrementItemsId++
        items.add(item)
    }

    override fun getItem(itemId: Int): Item {
        return items.find { it.id == itemId }
            ?: throw RuntimeException("Item with id $itemId not found")
    }

    override fun editItem(item: Item) {
        val oldItem = getItem(item.id)
        items.remove(oldItem)
        items.add(item)
    }

    override fun deleteItem(item: Item) {
        items.remove(item)
    }

    override fun getItemsForFolder(folderId: Int): List<Item> {
        return items.filter { it.folderId == folderId }.toList() // возвращаем копию
    }

}