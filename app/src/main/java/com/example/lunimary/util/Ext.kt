package com.example.lunimary.util

import com.example.lunimary.base.BaseResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

val Any?.isNull: Boolean get() = this == null

val <T> T?.notNull: T get() = this!!

fun HttpStatusCode.failed() : Boolean = !isSuccess()

