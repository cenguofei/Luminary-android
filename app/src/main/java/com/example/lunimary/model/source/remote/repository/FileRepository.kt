package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.UPLOAD_TYPE_ARTICLE_COVER
import com.example.lunimary.model.UploadData
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.FileSource

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