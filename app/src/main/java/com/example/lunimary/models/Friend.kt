package com.example.lunimary.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lunimary.util.Default
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 我的关注：
 * select * from friends
 *      where myId == Friends.userId
 *
 * 关注我的：
 * select * from friends
 *      where myId == Friends.whoId
 */
@kotlinx.serialization.Serializable
data class Friend(
    val id: Long = Long.Default,

    /**
     * 我的id
     */
    val userId: Long = Long.Default,

    /**
     * 我关注了谁
     */
    val whoId: Long = Long.Default,

    /**
     * 什么时候关注的
     */
    val timestamp: Long = Long.Default
) {
    /**
     * 成为朋友多少天
     */
    val howLong: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDateTime.parse(
            (System.currentTimeMillis() - timestamp).toString(), formatterToDay
        ).toLocalDate().toString()
}


@RequiresApi(Build.VERSION_CODES.O)
val formatterToDay: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")