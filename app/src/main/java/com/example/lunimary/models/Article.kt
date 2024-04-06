package com.example.lunimary.models

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.abs

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

    val timestamp: Long = System.currentTimeMillis()
) : java.io.Serializable, Parcelable {

    @Ignore
    constructor() : this(id = Long.Default)

    val daysFromToday: Long get() = getDaysSinceTimestamp(timestamp)

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

fun getDaysSinceTimestamp(timestamp: Long): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getDaysSinceTimestampApi26(timestamp)
    } else {
        getDaysSinceTimestampLessApi26(timestamp)
    }.also {
        "days=$it, less api 26=${getDaysSinceTimestampLessApi26(timestamp)}".logd("getDaysSinceTimestamp")
    }
}

/**
 * 将日期和时间分开处理，忽略了时间部分的差异，因此仅计算日期的差异
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun getDaysSinceTimestampApi26(timestamp: Long): Long {
    val currentDate = LocalDate.now()
    val dateFromTimestamp = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC).toLocalDate()

    val diffInDays = ChronoUnit.DAYS.between(dateFromTimestamp, currentDate)
    return abs(diffInDays)
}

/**
 * 在计算天数差异时，考虑了日期和时间的完整差异
 */
private fun getDaysSinceTimestampLessApi26(timestamp: Long): Long {
    val currentDate = Calendar.getInstance()
    val dateFromTimestamp = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    // 将日期设置为该天的开始时间，以忽略时间部分的差异
    currentDate.set(Calendar.HOUR_OF_DAY, 0)
    currentDate.set(Calendar.MINUTE, 0)
    currentDate.set(Calendar.SECOND, 0)
    currentDate.set(Calendar.MILLISECOND, 0)

    dateFromTimestamp.set(Calendar.HOUR_OF_DAY, 0)
    dateFromTimestamp.set(Calendar.MINUTE, 0)
    dateFromTimestamp.set(Calendar.SECOND, 0)
    dateFromTimestamp.set(Calendar.MILLISECOND, 0)

    val diffInMillis = currentDate.timeInMillis - dateFromTimestamp.timeInMillis

    return diffInMillis / (24 * 60 * 60 * 1000)
}