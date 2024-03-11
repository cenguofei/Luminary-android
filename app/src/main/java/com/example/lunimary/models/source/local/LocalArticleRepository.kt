package com.example.lunimary.models.source.local

import androidx.lifecycle.LiveData
import com.example.lunimary.models.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalArticleRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val dao = articleDao

    fun findArticlesByUsername(username: String): LiveData<List<Article>> =
        dao.findArticlesByUsername(username)

    suspend fun insertArticle(article: Article) {
        withContext(dispatcher) { dao.insertArticle(article) }
    }

    suspend fun deleteArticle(article: Article) {
        withContext(dispatcher) { dao.deleteArticle(article) }
    }

    suspend fun update(article: Article) {
        withContext(dispatcher) { dao.update(article) }
    }
}