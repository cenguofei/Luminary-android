package com.example.lunimary.base.storage

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.mmkv.decodeString
import com.example.lunimary.base.mmkv.encodeString
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import com.tencent.mmkv.MMKV

fun loadSession() : String {
    val username = currentUser.username
    val loadSession = decodeString(key = MMKVKeys.LUMINARY_SESSION_KEY, mmapID = username) ?: empty
    "loadSession, username=$username, loadSession=$loadSession".logd("cgf_security")
    return loadSession
}

fun saveSession(
    username: String,
    session: String
) {
    "saveSession, username=$username, session=$session".logd("cgf_security")
    session.encodeString(
        key = MMKVKeys.LUMINARY_SESSION_KEY,
        mmapID = username
    )
}

fun removeSession(username: String) {
    "removeSession, username=$username".logd("cgf_security")
    val mmkv = MMKV.mmkvWithID(username)
    mmkv.remove(MMKVKeys.LUMINARY_SESSION_KEY)
}

