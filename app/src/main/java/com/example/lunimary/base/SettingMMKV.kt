package com.example.lunimary.base

import com.example.lunimary.storage.MMKVKeys
import com.example.lunimary.util.boolean
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.delegate
import kotlin.properties.ReadWriteProperty

object SettingMMKV {
    var darkThemeSetting by darkTheme(
        key = MMKVKeys.DARK_THEME_SETTING,
        mmapID = currentUser.id.toString()
    )

    var userHasSetTheme by boolean(
        key = MMKVKeys.USER_HAS_SET_THEME,
        mmapID = currentUser.id.toString()
    )
}

fun darkTheme(
    key: String? = null,
    mmapID: String? = null,
    defaultValue: DarkThemeSetting = DarkThemeSetting.FollowSystem,
): ReadWriteProperty<Any, DarkThemeSetting> = delegate(
    key = key,
    defaultValue = defaultValue,
    mmapID = mmapID,
    getter = { k, v ->
        val value = when(v) {
            DarkThemeSetting.NightMode -> 0
            DarkThemeSetting.DarkMode -> 1
            else -> 2
        }
        when(decodeInt(k, value)) {
            0 -> DarkThemeSetting.NightMode
            1 -> DarkThemeSetting.DarkMode
            else -> DarkThemeSetting.FollowSystem
        }
    },
    setter = { k, v ->
        val value = when(v) {
            DarkThemeSetting.NightMode -> 0
            DarkThemeSetting.DarkMode -> 1
            else -> 2
        }
        encode(k, value)
    }
)

enum class DarkThemeSetting {
    DarkMode,
    NightMode,
    FollowSystem
}