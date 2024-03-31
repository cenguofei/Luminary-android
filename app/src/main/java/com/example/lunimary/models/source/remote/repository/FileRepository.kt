package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.UPLOAD_TYPE_ARTICLE_COVER
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.FileSource

class FileRepository : Repository by Repository() {
    private val source = FileSource

    suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int = UPLOAD_TYPE_ARTICLE_COVER
    ): DataResponse<UploadData> = withDispatcher {
        source.uploadFile(path, filename, uploadType)
    }
}