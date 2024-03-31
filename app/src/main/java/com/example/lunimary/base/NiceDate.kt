package com.example.lunimary.base

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
val dateTimeFormatterToSecond = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

@SuppressLint("SimpleDateFormat")
val dateTimeFormatterToDay: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

val Long.niceDateToSecond: String get() = dateTimeFormatterToSecond.format(this)

val Long.niceDateToDay: String get() = dateTimeFormatterToDay.format(this)

fun String.niceDateUtilDayToLong(split: String = "-"): Long {
    val instance = Calendar.getInstance()
    val arr = this.split(split)
    if (arr.size != 3) {
        return -1
    }
    var result = -1L
    runCatching {
        instance.set(arr[0].toInt(), arr[1].toInt()-1, arr[2].toInt())
        result = instance.timeInMillis
    }.onFailure {
        it.printStackTrace()
    }
    return result
}