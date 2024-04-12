package com.example.lunimary.base.mmkv

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.storage.MMKVKeys
import kotlin.properties.ReadWriteProperty

object SettingMMKV {
    var darkThemeSetting by darkTheme(
        key = MMKVKeys.DARK_THEME_SETTING_KEY,
        mmapID = currentUser.id.toString()
    )

    var userHasSetTheme by boolean(
        key = MMKVKeys.USER_HAS_SET_THEME_KEY,
        mmapID = currentUser.id.toString()
    )

    var hasLogout by boolean(
        key = MMKVKeys.USER_HAS_LOGOUT_KEY,
        defaultValue = true
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