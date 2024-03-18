package com.example.lunimary.models

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Entity
@kotlinx.serialization.Serializable
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = Long.Default,

    @ColumnInfo(name = "user_id") val userId: Long = Long.Default,

    val username: String = empty,

    val author: String = empty,

    val title: String = empty,

    val link: String = empty, // content

    val body: String = empty, // content

    /**
     * 文章可见范围
     */
    @ColumnInfo(name = "visible_mode") val visibleMode: VisibleMode = VisibleMode.PUBLIC,

    val tags: Array<String> = emptyArray(),

    val likes: Int = Int.Default,

    val collections: Int = Int.Default,

    val comments: Int = Int.Default,

    /**
     * 浏览数量
     */
    @ColumnInfo(name = "views_num") val viewsNum: Int = Int.Default,

    val cover: String = empty,

    val timestamp: Long = System.currentTimeMillis()
) : java.io.Serializable, Parcelable {

    @Ignore
    constructor() : this(id = Long.Default)

    val reallyCoverUrl: String get() = fileBaseUrl + cover


    /**
     * 文章发布了多少天
     */
    val daysFromToday: Int
        get() {
            return Int.Default
        }

    /**
     * format of publishTime
     */
    val niceDate: String get() = dateTimeFormat.format(timestamp)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (username != other.username) return false
        if (author != other.author) return false
        if (title != other.title) return false
        if (link != other.link) return false
        if (body != other.body) return false
        if (visibleMode != other.visibleMode) return false
        if (!tags.contentEquals(other.tags)) return false
        if (likes != other.likes) return false
        if (collections != other.collections) return false
        if (comments != other.comments) return false
        if (viewsNum != other.viewsNum) return false
        if (cover != other.cover) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + visibleMode.hashCode()
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + likes
        result = 31 * result + collections
        result = 31 * result + comments
        result = 31 * result + viewsNum
        result = 31 * result + cover.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

    companion object {
        val Default = Article(id = -1)
    }
}
@SuppressLint("SimpleDateFormat")
val dateTimeFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

//@RequiresApi(Build.VERSION_CODES.O)
//val formatterToSeconds: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")


enum class VisibleMode {
    OWN,
    PUBLIC,
    FRIEND
}

val testArticle = Article(
    userId = 10007,
    username = "ttt",
    author = "Chen Guofei",
    title = "Self study KTor development LuminaryBlog backend",
    body = "Note: Databases that support a path context root will have this value appended to the generated SQL path expression by default, so it is not necessary to include it in the provided argument String. In the above example, if MySQL is being used, the provided path arguments should be .name and .language respectively.",
    visibleMode = VisibleMode.PUBLIC,
    tags = arrayOf("Kotlin", "Compose", "Android", "Ktor"),
    collections = 99,
    comments = 99,
    likes = 99,
    viewsNum = 99
)