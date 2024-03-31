package com.example.lunimary.models

import com.example.lunimary.base.ktor.HOST
import com.example.lunimary.base.ktor.PORT
import com.example.lunimary.util.fileDownloadPath

@kotlinx.serialization.Serializable
data class UploadData(
    val filenames: List<String>,
) {
    fun first(): String = filenames[0]
}

const val UPLOAD_TYPE_USER_HEAD = 0
const val UPLOAD_TYPE_ARTICLE_COVER = 1
const val UPLOAD_TYPE_OTHER = 2
const val UPLOAD_TYPE_USER_BACKGROUND = 3

val fileBaseUrl =  "http://$HOST:$PORT$fileDownloadPath?file_url="
