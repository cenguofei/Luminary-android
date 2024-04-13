package com.example.lunimary.base.viewmodel

import com.example.lunimary.util.unknownErrorMsg

open class BaseViewModel : NetworkViewModel()

val Throwable.errorMsg: String get() = message ?: unknownErrorMsg