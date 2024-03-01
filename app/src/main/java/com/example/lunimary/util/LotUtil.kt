package com.example.lunimary.util

import android.util.Log

fun String.logv(tag:String = "cgf_verbose") {
    Log.v(tag,this)
}

fun String.logi(tag:String = "cgf_info") {
    Log.i(tag,this)
}

fun String?.logd(tag:String = "cgf_debug") {
    Log.d(tag,this ?: "null")
}

fun String.loge(tag:String = "cgf_error") {
    Log.e(tag,this)
}