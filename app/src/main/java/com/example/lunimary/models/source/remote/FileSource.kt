package com.example.lunimary.models.source.remote

import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.FileSourceImpl

interface FileSource {
    suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int
    ): DataResponse<UploadData>

    companion object : FileSource by FileSourceImpl()
}