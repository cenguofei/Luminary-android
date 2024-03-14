package com.example.lunimary.models.source.remote

import com.example.lunimary.models.UploadData
import com.example.lunimary.models.responses.DataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val source: FileSource = FileSourceImpl()

    suspend fun uploadFile(
        path: String,
        filename: String
    ): DataResponse<UploadData> = withContext(dispatcher) {
        source.uploadFile(path, filename)
    }
}