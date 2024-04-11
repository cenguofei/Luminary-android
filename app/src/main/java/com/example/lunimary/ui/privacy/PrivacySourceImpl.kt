package com.example.lunimary.ui.privacy

import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl
import io.ktor.client.request.get

class PrivacySourceImpl : PrivacySource, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun getPrivacy(): DataResponse<String> {
        return client.get(urlString = "/privacy").init()
    }
}