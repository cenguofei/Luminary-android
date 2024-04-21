package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.Topic
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.TopicSource
import com.example.lunimary.model.source.remote.impl.TopicSourceImpl

class TopicRepository : Repository by Repository() {
    private val topicSource: TopicSource = TopicSourceImpl()

    suspend fun userTopics(userId: Long): DataResponse<List<Topic>> {
        return withDispatcher { topicSource.userTopics(userId) }
    }

    suspend fun recommendTopics(): DataResponse<List<Topic>>  {
        return withDispatcher { topicSource.recommendTopics() }
    }

    suspend fun createOrUpdate(userId: Long, topics: List<Long>): DataResponse<Unit> {
        return withDispatcher { topicSource.createOrUpdate(userId, topics) }
    }
}