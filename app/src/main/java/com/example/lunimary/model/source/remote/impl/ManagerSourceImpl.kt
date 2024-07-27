package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.model.PublishState
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.ManagerSource
import com.example.lunimary.util.approveArticlePath

class ManagerSourceImpl : BaseSourceImpl by BaseSourceImpl(), ManagerSource {

    override suspend fun audit(state: PublishState): DataResponse<Unit> {
        return client.securityGet(approveArticlePath).init()
    }
}