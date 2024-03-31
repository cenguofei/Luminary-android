package com.example.lunimary.util

import android.util.Log

var DEBUG = true

fun String.logv(tag:String = "cgf_verbose") {
    if (DEBUG) {
        Log.v(tag,this)
    }
}

fun String.logi(tag:String = "cgf_info") {
    if (DEBUG) {
        Log.i(tag,this)
    }
}

fun String?.logd(tag:String = "cgf_debug") {
    if (DEBUG) {
        Log.d(tag, this ?: "null")
    }
}

fun String.loge(tag:String = "cgf_error") {
    if (DEBUG) {
        Log.e(tag,this)
    }
}