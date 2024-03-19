package com.example.lunimary.util

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.lunimary.base.BaseResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

val Any?.isNull: Boolean get() = this == null

val <T> T?.notNull: T get() = this!!

fun HttpStatusCode.failed() : Boolean = !isSuccess()

fun stringResources(context: Context, @StringRes id: Int): String {
    return context.resources.getString(id)
}
