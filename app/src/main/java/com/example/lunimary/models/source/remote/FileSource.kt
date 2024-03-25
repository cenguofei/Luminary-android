package com.example.lunimary.models.source.remote

import com.example.lunimary.models.UPLOAD_TYPE_ARTICLE_COVER
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse

interface FileSource {
    suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int
    ): DataResponse<UploadData>
}