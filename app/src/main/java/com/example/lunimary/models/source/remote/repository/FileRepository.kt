package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.UPLOAD_TYPE_ARTICLE_COVER
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.FileSource
import com.example.lunimary.models.source.remote.impl.FileSourceImpl

class FileRepository : BaseRepository by BaseRepository() {
    private val source: FileSource = FileSourceImpl()

    suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int = UPLOAD_TYPE_ARTICLE_COVER
    ): DataResponse<UploadData> = withDispatcher {
        source.uploadFile(path, filename, uploadType)
    }
}