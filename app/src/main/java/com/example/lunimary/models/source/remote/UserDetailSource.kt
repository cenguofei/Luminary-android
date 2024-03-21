package com.example.lunimary.models.source.remote

import com.example.lunimary.models.User
import com.example.lunimary.models.responses.DataResponse

interface UserDetailSource {
    suspend fun likesOfUser(): DataResponse<Long>

    suspend fun followings(): DataResponse<List<User>>

    suspend fun followers(): DataResponse<List<User>>
}