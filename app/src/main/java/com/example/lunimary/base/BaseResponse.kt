package com.example.lunimary.base

import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse<T> {
    var code: Int = -1
        set(value) {
            if (code == -1) {
                field = value
            }
        }

    var msg: String = empty

    open var data: T? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    fun isSuccess(): Boolean = code in (200 until 300)

    inline fun <reified R> init(response: HttpResponse): R {
        "code=${response.status}, ${response.request.url}".logd()
        code = response.status.value
        return this as R
    }
}
