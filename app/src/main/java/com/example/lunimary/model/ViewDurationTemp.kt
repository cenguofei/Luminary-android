package com.example.lunimary.model

import com.example.lunimary.util.Default

/**
 * �û��������ʱ���࣬�����Ƽ�����
 */
@kotlinx.serialization.Serializable
data class ViewDurationTemp(
    val tags: List<String> = emptyList(),
    val userId: Long = Long.Default,
    val duration: Long = Long.Default,
    val timestamp: Long = Long.Default
)