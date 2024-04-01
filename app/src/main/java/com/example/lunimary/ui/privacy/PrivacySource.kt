package com.example.lunimary.ui.privacy

import com.example.lunimary.models.responses.DataResponse

interface PrivacySource {

    suspend fun getPrivacy(): DataResponse<String>
}