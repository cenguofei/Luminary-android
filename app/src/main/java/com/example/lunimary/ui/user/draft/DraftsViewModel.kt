package com.example.lunimary.ui.user.draft

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.local.LocalArticleRepository
import com.example.lunimary.base.currentUser
import kotlinx.coroutines.launch

class DraftsViewModel: BaseViewModel() {
    private val repository = LocalArticleRepository()

    fun findDrafts(): LiveData<List<Article>> = repository.findArticlesByUsername(currentUser.username)

    fun remove(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }

    fun insertArticle(article: Article) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }
}