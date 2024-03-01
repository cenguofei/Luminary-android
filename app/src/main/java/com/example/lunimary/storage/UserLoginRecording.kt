package com.example.lunimary.storage

import com.example.lunimary.models.User
import com.example.lunimary.util.decodeParcelable
import com.example.lunimary.util.encodeParcelable
import com.example.lunimary.util.logd
import com.tencent.mmkv.MMKV

fun lastLoginUser(): User? = decodeParcelable(MMKVKeys.LAST_LOGIN_USER)

fun saveLastLoginUser(user: User?) {
    "saveLastLoginUser".logd()
    if (user == null) return
    user.encodeParcelable(key = MMKVKeys.LAST_LOGIN_USER)
}

fun removeLastLoginUser() {
    val mmkv = MMKV.defaultMMKV()
    mmkv.remove(MMKVKeys.LAST_LOGIN_USER)
}