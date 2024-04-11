package com.example.lunimary.model

import com.example.lunimary.util.Default

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
)