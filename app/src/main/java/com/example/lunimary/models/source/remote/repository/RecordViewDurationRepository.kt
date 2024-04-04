package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.ViewDurationTemp
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.RecordViewTime

class RecordViewDurationRepository: Repository by Repository()  {
    private val source = RecordViewTime

    suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit> =
        withDispatcher { source.record(viewDuration) }
}