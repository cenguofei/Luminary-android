package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.ViewDurationTemp
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.RecordViewTime

class RecordViewDurationRepository: Repository by Repository()  {
    private val source = RecordViewTime

    suspend fun record(viewDuration: ViewDurationTemp): DataResponse<Unit> =
        withDispatcher { source.record(viewDuration) }
}