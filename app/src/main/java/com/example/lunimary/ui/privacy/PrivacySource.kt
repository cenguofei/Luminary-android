package com.example.lunimary.ui.privacy

import com.example.lunimary.model.responses.DataResponse

interface PrivacySource {

    suspend fun getPrivacy(): DataResponse<String>
}