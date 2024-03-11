package com.example.lunimary.models.source.local

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.lunimary.LuminaryApplication.Companion.applicationContext
import com.example.lunimary.models.Article
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@TypeConverters(
    Converters::class,
)
@Database(
    entities = [Article::class, Tag::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile private var Instance: AppDatabase? = null

        fun getDatabase(context: Context = applicationContext): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "lunimary_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

val articleDao: ArticleDao get() = AppDatabase.getDatabase().articleDao()

class Converters {
    @TypeConverter
    fun stringToTags(array: String): Array<String> {
        return Json.decodeFromString(array)
    }

    @TypeConverter
    fun tagsToString(array: Array<String>): String {
        return Json.encodeToString(array)
    }
}