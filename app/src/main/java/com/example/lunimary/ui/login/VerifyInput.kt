package com.example.lunimary.ui.login

import com.example.lunimary.util.empty

/**
 * 校验用户名和密码是否符合要求
 * @return [Pair] first: 是否通过，second: 为什么不通过
 */
fun verifyInput(
    username: String,
    password: String
): Pair<Boolean, String> {
    if (username.isBlank() || password.isBlank()) {
        return false to "Username or password cannot be empty."
    }

    if (!isUsernameValid(username)) {
        return false to "$username is not a valid username. " +
                "The username cannot contain special characters."
    }

    if (!isPasswordValid(password, false)) {
        return false to "$password is not a valid password. " +
                "Passwords can only be a combination of numbers, letters, and special symbols"
    }

    if (password.length < PASSWORD_LENGTH) {
        return false to "The password length cannot be less than $PASSWORD_LENGTH."
    }

    return true to empty
}

const val PASSWORD_LENGTH = 6

/**
 * 只能包含字母、数字、汉字或其他文字，不能包含+-=/等符号
 */
fun isUsernameValid(username: String): Boolean {
    val pattern = Regex("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    return pattern.matches(username)
}

/**
 * 密码只能包含字母、数字或特殊英文符号
 */
fun isPasswordValid(
    password: String,
    checkLength: Boolean = true,
): Boolean {
    if (checkLength && password.length < PASSWORD_LENGTH) {
        return false
    }
    val pattern = Regex("^[a-zA-Z0-9+\\-*/@!#%&_=.,?;:$~`]+$")
    return pattern.matches(password)
}