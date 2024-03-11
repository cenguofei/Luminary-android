package com.example.lunimary.models.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {
    @Query("SELECT * FROM Tag WHERE Tag.username == :username")
    fun getHistoryTags(username: String): LiveData<List<Tag>>

    @Insert
    fun createTag(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)
}