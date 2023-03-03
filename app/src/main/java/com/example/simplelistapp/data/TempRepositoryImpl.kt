package com.example.simplelistapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplelistapp.domain.ItemsFolder
import com.example.simplelistapp.domain.Item
import com.example.simplelistapp.domain.Repository

object TempRepositoryImpl : Repository {

    private val foldersLD = MutableLiveData<List<ItemsFolder>>()
    private val itemsLD = MutableLiveData<List<Item>>()

    private val folders = mutableListOf<ItemsFolder>()

    private val compareById = Comparator<Item> { p0, p1 -> p0.id.compareTo(p1.id) }
    private val compareByEnable = Comparator<Item> { p0, p1 -> p0.enabled.compareTo(p1.enabled) }
    private val items = sortedSetOf<Item>(compareByEnable.then(compareById))

    private var autoincrementFolderId = 0
    private var autoincrementItemsId = 0

    init {
//        for (i in 1..3) {
//            val folder = ItemsFolder("Folder Num. $i")
//            addFolder(folder)
//        }

        for (i in 1..3) {
            val item = Item(1, "Item$i", 1)
            addItem(item)
        }

//        TODO("Сгенерить айтемы")
    }

    override fun addFolder(folder: ItemsFolder) {
        folder.id = autoincrementFolderId++
        folders.add(folder)
        updateFoldersList()
    }

    override fun getFolder(folderId: Int): ItemsFolder {
        return folders.find { it.id == folderId }
            ?: throw RuntimeException("Folder with id $folderId not found")
    }

    override fun editFolder(folder: ItemsFolder) {
        val oldFolder = getFolder(folder.id)
        folders.remove(oldFolder)
        folders.add(folder)
        updateFoldersList()
    }

    override fun deleteFolder(folder: ItemsFolder) {
        folders.remove(folder)
        updateFoldersList()
    }

    override fun getFoldersList(): LiveData<List<ItemsFolder>> {
        return foldersLD
    }

    override fun addItem(item: Item) {
        item.id = autoincrementItemsId++
        items.add(item)
        updateItemsList(item.folderId)
    }

    override fun getItem(itemId: Int): Item {
        return items.find { it.id == itemId }
            ?: throw RuntimeException("Item with id $itemId not found")
    }

    override fun editItem(item: Item) {
        val oldItem = getItem(item.id)
        items.remove(oldItem)
        items.add(item)
        updateItemsList(item.folderId)
    }

    override fun deleteItem(item: Item) {
        items.remove(item)
        updateItemsList(item.folderId)
    }

    override fun getItemsForFolder(folderId: Int): LiveData<List<Item>> {
        updateItemsList(folderId)
        return itemsLD
    }

    private fun updateFoldersList() {
        foldersLD.value = folders.toList() // возвращаем копию
    }

    private fun updateItemsList(folderId: Int) {
        itemsLD.value = items.filter { it.folderId == folderId }.toList() // возвращаем копию
    }

}