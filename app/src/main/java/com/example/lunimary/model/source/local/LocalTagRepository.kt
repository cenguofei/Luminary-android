package com.example.lunimary.model.source.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalTagRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val dao = AppDatabase.getDatabase().tagDao()

    fun getHistoryTags(username: String): LiveData<List<Tag>> = dao.getHistoryTags(username)

    suspend fun createTag(tag: Tag) {
        withContext(dispatcher) { dao.createTag(tag) }
    }

    suspend fun deleteTag(tag: Tag) {
        withContext(dispatcher) { dao.deleteTag(tag) }
    }
}