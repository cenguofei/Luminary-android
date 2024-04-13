package com.example.lunimary.ui.user.draft

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.Article
import com.example.lunimary.model.source.local.LocalArticleRepository
import kotlinx.coroutines.launch

class DraftsViewModel: BaseViewModel() {
    private val repository = LocalArticleRepository()

    fun findDrafts(): LiveData<List<Article>> = repository.findArticlesByUsername(currentUser.username)

    fun remove(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}