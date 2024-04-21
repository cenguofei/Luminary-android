package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.Topic
import com.example.lunimary.model.UserSelectedTopics
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.TopicSource
import com.example.lunimary.util.createOrUpdateTopicsPath
import com.example.lunimary.util.recommendTopicsPath
import com.example.lunimary.util.userSelectedTopicsPath
import io.ktor.client.request.get

class TopicSourceImpl : BaseSourceImpl by BaseSourceImpl(), TopicSource {

    override suspend fun userTopics(userId: Long): DataResponse<List<Topic>> {
        return client.securityGet(urlString = userSelectedTopicsPath).init()
    }

    override suspend fun recommendTopics(): DataResponse<List<Topic>> {
        return client.get(recommendTopicsPath).init()
    }

    override suspend fun createOrUpdate(userId: Long, topics: List<Long>): DataResponse<Unit> {
        return client.securityPost(urlString = createOrUpdateTopicsPath){
            setJsonBody(
                UserSelectedTopics(
                    userId = userId,
                    topics = topics.toTypedArray()
                )
            )
        }.init()
    }
}