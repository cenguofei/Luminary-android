package com.example.lunimary.base.storage

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.mmkv.decodeString
import com.example.lunimary.util.empty
import com.example.lunimary.base.mmkv.encodeString
import com.tencent.mmkv.MMKV

fun loadSession(
    key: String
) : String = decodeString(key, currentUser.username) ?: empty

fun saveSession(
    username: String,
    session: String
) { session.encodeString(MMKVKeys.LUMINARY_SESSION, username) }

fun removeSession() {
    val mmkv = MMKV.mmkvWithID(currentUser.username)
    mmkv.remove(MMKVKeys.LUMINARY_SESSION)
}

