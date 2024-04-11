package com.example.lunimary.model.source.remote

import com.example.lunimary.model.ViewDurationTemp
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.RecordViewTimeImpl

interface RecordViewTime {

    suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit>

    companion object : RecordViewTime by RecordViewTimeImpl()
}