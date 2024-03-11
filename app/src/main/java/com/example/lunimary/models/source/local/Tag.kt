package com.example.lunimary.models.source.local

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty
import io.ktor.util.reflect.Type

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    var id: Long = Long.Default,

    @ColumnInfo("name")
    var name: String = empty,

    @ColumnInfo("color")
    var color: Int = Color.Gray.toArgb(),

    @ColumnInfo("username")
    var username: String = empty
) {
    @Ignore constructor(): this(id = Long.Default)
}