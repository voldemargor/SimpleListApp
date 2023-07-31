package com.vladimirgorshkov.simplelistapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladimirgorshkov.simplelistapp.domain.Folder
import com.vladimirgorshkov.simplelistapp.domain.Item
import com.vladimirgorshkov.simplelistapp.domain.Repository

object TempRepositoryImpl : Repository {

    private val foldersLD = MutableLiveData<List<Folder>>()
    private val itemsLD = MutableLiveData<List<Item>>()

    private val folders = mutableListOf<Folder>()

    private val compareById = Comparator<Item> { p0, p1 -> p0.id.compareTo(p1.id) }
    private val compareByEnable = Comparator<Item> { p0, p1 -> p0.enabled.compareTo(p1.enabled) }
    private val items = sortedSetOf<Item>(compareByEnable.then(compareById))

    private var autoincrementFolderId = 0
    private var autoincrementItemsId = 0

    //    init {
    //        // Генерим фолдеры
    //        for (i in 0..4) {
    //            val folder = Folder("Folder Num. $i", id = i)
    //            addFolder(folder)
    //        }
    //
    //        // Генерим айтемы для фолдеров
    //        for (f in 0 until folders.size) {
    //            for (i in 1..3) {
    //                val item = Item(folderId = f, "Folder$f-Item$i", 1)
    //                addItem(item)
    //            }
    //        }
    //    }

    override suspend fun addFolder(folder: Folder): Long {
        folder.id = autoincrementFolderId++
        folders.add(folder)
        updateFoldersList()
        return folder.id.toLong()
    }

    override suspend fun getFolder(folderId: Int): Folder {
        Log.d("mylog", "getFolder: $folderId")
        return folders.find { it.id == folderId }
            ?: throw RuntimeException("Folder with id $folderId not found")
    }

    override suspend fun editFolder(folder: Folder) {
        val oldFolder = getFolder(folder.id)
        folders.remove(oldFolder)
        folders.add(folder)
        updateFoldersList()
    }

    override suspend fun deleteFolder(folder: Folder) {
        folders.remove(folder)
        updateFoldersList()
    }

    override fun getFoldersList(): LiveData<List<Folder>> {
        return foldersLD
    }

    override suspend fun addItem(item: Item) {
        item.id = autoincrementItemsId++
        items.add(item)
        updateItemsList(item.folderId)
    }

    override suspend fun getItem(itemId: Int): Item {
        return items.find { it.id == itemId }
            ?: throw RuntimeException("Item with id $itemId not found")
    }

    override suspend fun editItem(item: Item) {
        val oldItem = getItem(item.id)
        items.remove(oldItem)
        items.add(item)
        updateItemsList(item.folderId)
    }

    override suspend fun deleteItem(item: Item) {
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