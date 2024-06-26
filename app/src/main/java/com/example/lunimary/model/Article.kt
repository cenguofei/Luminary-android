package com.example.lunimary.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.lunimary.base.TimeManager.getDaysSinceTimestamp
import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty
import kotlinx.android.parcel.Parcelize

@Entity
@kotlinx.serialization.Serializable
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = Long.Default,

    @ColumnInfo(name = "user_id") val userId: Long = Long.Default,

    val username: String = empty,

    val author: String = empty,

    val title: String = empty,

    val link: String = empty, //文章链接

    val body: String = empty, // content

    @ColumnInfo(name = "visible_mode") val visibleMode: VisibleMode = VisibleMode.PUBLIC,

    val tags: Array<String> = emptyArray(),

    val likes: Int = Int.Default,

    val collections: Int = Int.Default,

    val comments: Int = Int.Default,

    @ColumnInfo(name = "views_num") val viewsNum: Int = Int.Default,

    val cover: String = empty,

    val timestamp: Long = Long.Default
) : java.io.Serializable, Parcelable {

    @Ignore
    constructor() : this(id = Long.Default)

    val daysFromToday: String get() = getDaysSinceTimestamp(timestamp)

    /**
     * format of publishTime
     */
    val niceDate: String get() = timestamp.niceDateToDay

    val isLunimaryStation: Boolean get() = body.isNotBlank()

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

enum class VisibleMode(val modeName: String) {
    OWN("仅自己可见"),
    PUBLIC("公开可见"),
    FRIEND("互关朋友可见")
}