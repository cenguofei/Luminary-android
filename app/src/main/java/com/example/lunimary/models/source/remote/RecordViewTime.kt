package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ViewDurationTemp
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.RecordViewTimeImpl

interface RecordViewTime {

    suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit>

    companion object : RecordViewTime by RecordViewTimeImpl()
}