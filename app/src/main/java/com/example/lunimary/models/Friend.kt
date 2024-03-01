package com.example.lunimary.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lunimary.util.Default
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * �ҵĹ�ע��
 * select * from friends
 *      where myId == Friends.userId
 *
 * ��ע�ҵģ�
 * select * from friends
 *      where myId == Friends.whoId
 */
@kotlinx.serialization.Serializable
data class Friend(
    val id: Long = Long.Default,

    /**
     * �ҵ�id
     */
    val userId: Long = Long.Default,

    /**
     * �ҹ�ע��˭
     */
    val whoId: Long = Long.Default,

    /**
     * ʲôʱ���ע��
     */
    val timestamp: Long = Long.Default
) {
    /**
     * ��Ϊ���Ѷ�����
     */
    val howLong: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDateTime.parse(
            (System.currentTimeMillis() - timestamp).toString(), formatterToDay
        ).toLocalDate().toString()
}


@RequiresApi(Build.VERSION_CODES.O)
val formatterToDay: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")