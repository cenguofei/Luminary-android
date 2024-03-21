package com.example.lunimary.storage

import com.example.lunimary.util.currentUser
import com.example.lunimary.util.decodeString
import com.example.lunimary.util.empty
import com.example.lunimary.util.encodeString
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

