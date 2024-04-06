package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.models.ViewDurationTemp
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.RecordViewTime
import com.example.lunimary.util.viewArticleDurationPath
import io.ktor.client.request.post

class RecordViewTimeImpl : RecordViewTime, BaseSourceImpl by BaseSourceImpl() {

    override suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit> {
        return client.post(viewArticleDurationPath) {
            setJsonBody(viewDuration)
        }.init()
    }
}