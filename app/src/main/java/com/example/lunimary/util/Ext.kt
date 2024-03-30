package com.example.lunimary.util

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

val <T> T?.notNull: T get() = this!!

fun HttpStatusCode.failed() : Boolean = !isSuccess()