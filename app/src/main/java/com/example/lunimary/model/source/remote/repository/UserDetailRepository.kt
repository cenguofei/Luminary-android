package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.UserDetailSource

class UserDetailRepository : Repository by Repository() {
    private val userDetailSource = UserDetailSource
    suspend fun interactionData(id: Long = 0L): DataResponse<InteractionData> =
        withDispatcher { userDetailSource.interactionData(id) }
}