package com.example.lunimary.models.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lunimary.models.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM Article WHERE Article.username == :username")
    fun findArticlesByUsername(
        username: String
    ): LiveData<List<Article>>

    @Insert
    fun insertArticle(article: Article)

    @Delete
    fun deleteArticle(article: Article)

    @Update
    fun update(article: Article)
}