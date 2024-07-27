package com.example.lunimary.model.source.remote

import com.example.lunimary.model.PublishState
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.ManagerSourceImpl

interface ManagerSource {

    suspend fun audit(
        state: PublishState
    ): DataResponse<Unit>

    companion object : ManagerSource by ManagerSourceImpl()
}