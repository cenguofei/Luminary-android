package com.example.lunimary.model.source.remote

import com.example.lunimary.model.UploadData
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.FileSourceImpl

interface FileSource {
    suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int
    ): DataResponse<UploadData>

    companion object : FileSource by FileSourceImpl()
}