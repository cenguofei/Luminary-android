package com.example.lunimary.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by TanJiaJun on 2020-01-11.
 */
inline fun <T> delegate(
    key: String? = null,
    defaultValue: T,
    mmapID: String? = null,
    crossinline getter: MMKV.(key: String, defaultValue: T) -> T,
    crossinline setter: MMKV.(key: String, value: T) -> Boolean
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return mmkv.getter(key ?: property.name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        mmkv.setter(key ?: property.name, value)
    }
}

fun boolean(
    key: String? = null,
    defaultValue: Boolean = false,
    mmapID: String? = null
): ReadWriteProperty<Any, Boolean> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeBool(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun int(
    key: String? = null,
    defaultValue: Int = 0,
    mmapID: String? = null
): ReadWriteProperty<Any, Int> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeInt(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun long(
    key: String? = null,
    defaultValue: Long = 0L,
    mmapID: String? = null
): ReadWriteProperty<Any, Long> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeLong(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun float(
    key: String? = null,
    defaultValue: Float = 0.0F,
    mmapID: String? = null
): ReadWriteProperty<Any, Float> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeFloat(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun double(
    key: String? = null,
    defaultValue: Double = 0.0,
    mmapID: String? = null
): ReadWriteProperty<Any, Double> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeDouble(k, v) },
    setter = { k, v -> encode(k, v) }
)


private inline fun <T> nullableDefaultValueDelegate(
    key: String? = null,
    defaultValue: T,
    mmapID: String? = null,
    crossinline getter: MMKV.(String, T?) -> T?,
    crossinline setter: MMKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            mmkv.getter(key ?: property.name, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            mmkv.setter(key ?: property.name, value)
        }
    }

fun byteArray(
    key: String? = null,
    mmapID: String? = null,
    defaultValue: ByteArray = byteArrayOf(),
): ReadWriteProperty<Any, ByteArray> = nullableDefaultValueDelegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeBytes(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun string(
    key: String? = null,
    mmapID: String? = null,
    defaultValue: String = empty
): ReadWriteProperty<Any, String> = nullableDefaultValueDelegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeString(k, v) },
    setter = { k, v -> encode(k, v) }
)

fun stringSet(
    key: String? = null,
    mmapID: String? = null,
    defaultValue: Set<String> = emptySet()
): ReadWriteProperty<Any, Set<String>> = nullableDefaultValueDelegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v -> decodeStringSet(k, v) },
    setter = { k, v -> encode(k, v) }
)

inline fun <reified T : Parcelable> parcelable(
    key: String? = null,
    mmapID: String? = null,
    defaultValue: T
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    val mmkv = if (mmapID != null) MMKV.mmkvWithID(mmapID) else MMKV.defaultMMKV()
    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        mmkv.decodeParcelable(key ?: property.name, T::class.java, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        mmkv.encode(key ?: property.name, value)
    }
}

inline fun <reified T : Parcelable> decodeParcelable(
    key: String,
    mmapID: String? = null
): T? {
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
): String? {
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