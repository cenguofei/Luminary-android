package com.example.lunimary.model.source.remote

import com.example.lunimary.model.Topic
import com.example.lunimary.model.responses.DataResponse

interface TopicSource {
    suspend fun userTopics(
        userId: Long
    ): DataResponse<List<Topic>>

    suspend fun recommendTopics(): DataResponse<List<Topic>>

    suspend fun createOrUpdate(
        userId: Long,
        topics: List<Long>
    ): DataResponse<Unit>
}