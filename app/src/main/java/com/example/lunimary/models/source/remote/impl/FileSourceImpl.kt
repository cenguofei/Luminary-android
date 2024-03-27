package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.UploadData
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.setBearAuth
import com.example.lunimary.models.ktor.setSession
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.FileSource
import com.example.lunimary.util.fileUploadPath
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File

class FileSourceImpl : BaseSourceImpl by BaseSourceImpl(), FileSource {
    override suspend fun uploadFile(
        path: String,
        filename: String,
        uploadType: Int
    ): DataResponse<UploadData> {
        val file = File(path)
        return client.submitFormWithBinaryData(
            url = fileUploadPath,
            formData = formData {
                append(
                    key = "image",
                    value = file.readBytes(),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                    }
                )
            },
            block = {
                setSession()
                setBearAuth()
                url { parameters.append("upload_type", uploadType.toString()) }
            }
        ).init()
    }
}