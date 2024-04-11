package com.example.lunimary.model

import android.os.Parcelable
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val id: Long = Long.Default,
    val username: String,
    val age: Int = Int.Default,
    val sex: Sex = Sex.Sealed,
    @SerialName("head_url")
    val headUrl: String = "res/uploads/default_bg.jpg",
    val background: String = "res/uploads/default_head_img.png",
    val password: String = empty,
    val role: Role = Role.User,
    val status: UserStatus = UserStatus.Normal,

    val birth: Long = Long.Default,
    val signature: String = empty,
    val location: String = empty,
    @SerialName("blog_address")
    val blogAddress: String = "https://github.com/cenguofei"
) : java.io.Serializable, Parcelable {

    fun realHeadUrl(): String = fileBaseUrl + headUrl

    fun realBackgroundUrl(): String = fileBaseUrl + background
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (age != other.age) return false
        if (sex != other.sex) return false
        if (headUrl != other.headUrl) return false
        if (background != other.background) return false
        if (password != other.password) return false
        if (role != other.role) return false
        if (status != other.status) return false
        if (birth.niceDateToDay != other.birth.niceDateToDay) return false
        if (signature != other.signature) return false
        if (location != other.location) return false
        if (blogAddress != other.blogAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + age
        result = 31 * result + sex.hashCode()
        result = 31 * result + headUrl.hashCode()
        result = 31 * result + background.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + birth.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + blogAddress.hashCode()
        return result
    }


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

enum class Sex(val text: String) {
    Male("ÄÐ"),
    Female("Å®"),
    Sealed("Î´Öª");
}

fun byText(text: String): Sex {
    if (text == "ÄÐ") {
        return Sex.Male
    }
    if (text == "Å®") {
        return Sex.Female
    }
    return Sex.Sealed
}

enum class Role {
    User,
    Manager
}

enum class UserStatus {
    Normal,
    Deleted
}
