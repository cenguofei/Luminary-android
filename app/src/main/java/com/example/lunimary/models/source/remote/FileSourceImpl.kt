package com.example.lunimary.models.source.remote

import com.example.lunimary.models.UPLOAD_TYPE_ARTICLE_COVER
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.setBearAuth
import com.example.lunimary.models.ktor.setSession
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.util.fileUploadPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import java.io.File

class FileSourceImpl(
    private val client: HttpClient = httpClient
) : FileSource {
    override suspend fun uploadFile(
        path: String,
        filename: String
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
                url { parameters.append("upload_type", UPLOAD_TYPE_ARTICLE_COVER.toString()) }
            }
        ).let { it.body<DataResponse<UploadData>>().init(it) }
    }
}