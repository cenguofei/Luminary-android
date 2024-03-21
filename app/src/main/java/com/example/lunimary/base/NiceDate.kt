package com.example.lunimary.base

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
val dateTimeFormatterToSecond = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

@SuppressLint("SimpleDateFormat")
val dateTimeFormatterToDay: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

val Long.niceDateToSecond: String get() = dateTimeFormatterToSecond.format(this)

val Long.niceDateToDay: String get() = dateTimeFormatterToDay.format(this)