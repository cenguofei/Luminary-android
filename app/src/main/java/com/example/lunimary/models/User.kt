package com.example.lunimary.models

import android.os.Parcelable
import com.example.lunimary.base.currentUser
import com.example.lunimary.util.logd
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val id: Long = 0,
    val username: String,
    val age: Int = 0,
    val sex: Sex = Sex.Sealed,
    @SerialName("head_url")
    val headUrl: String = "123.png",
    val background: String = "123.png",
    val password: String = "",
    val role: Role = Role.User,
    val status: UserStatus = UserStatus.Normal
) : java.io.Serializable, Parcelable {

    fun realHeadUrl(): String = fileBaseUrl + headUrl

    fun realBackgroundUrl(): String = fileBaseUrl + background
    companion object {
        val NONE = User(username = "luminary-default-user")
    }
}

fun checkUserNotNone(action: () -> Unit) {
    if (currentUser != User.NONE) {
        action()
    } else {
        "The current user is NONE.".logd()
    }
}

enum class Sex {
    Male,
    Female,
    Sealed
}

enum class Role {
    User,
    Manager
}

enum class UserStatus {
    Normal,
    Deleted
}
