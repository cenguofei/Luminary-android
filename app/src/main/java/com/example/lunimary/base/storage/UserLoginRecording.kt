package com.example.lunimary.base.storage

import com.example.lunimary.models.User
import com.example.lunimary.base.mmkv.decodeParcelable
import com.example.lunimary.base.mmkv.encodeParcelable
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