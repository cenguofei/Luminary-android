package com.example.lunimary.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV

inline fun <reified T : Parcelable> decodeParcelable(
    key: String,
    mmapID: String? = null
) : T? {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    return mmkv.decodeParcelable(key, T::class.java)
}

inline fun <reified T : Parcelable> T.encodeParcelable(
    key: String,
    mmapID: String? = null
) {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    mmkv.encode(key, this)
}

fun decodeString(
    key: String,
    mmapID: String? = null
) : String? {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    return mmkv.decodeString(key)
}

fun String.encodeString(
    key: String,
    mmapID: String? = null
) {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    mmkv.encode(key, this)
}

fun Set<String>.encodeSet(
    key: String,
    mmapID: String? = null
) {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    mmkv.encode(key, this)
}

fun decodeSet(
    key: String,
    mmapID: String? = null
): Set<String> {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    val set = mmkv.decodeStringSet(key)
    return set ?: emptySet()
}