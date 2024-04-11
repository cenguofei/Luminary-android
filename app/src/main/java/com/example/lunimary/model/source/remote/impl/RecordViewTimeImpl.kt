package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.ViewDurationTemp
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.RecordViewTime
import com.example.lunimary.util.viewArticleDurationPath
import io.ktor.client.request.post

class RecordViewTimeImpl : RecordViewTime, BaseSourceImpl by BaseSourceImpl() {

    override suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit> {
        return client.post(viewArticleDurationPath) {
            setJsonBody(viewDuration)
        }.init()
    }
}