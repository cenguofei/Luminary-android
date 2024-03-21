package com.example.lunimary.models.source.remote

import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse

interface FileSource {
    suspend fun uploadFile(
        path: String,
        filename: String
    ): DataResponse<UploadData>
}