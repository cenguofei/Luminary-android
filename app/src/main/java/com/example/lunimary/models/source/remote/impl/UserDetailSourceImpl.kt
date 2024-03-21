package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.User
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.UserDetailSource
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.likesOfUserPath
import com.example.lunimary.util.myFollowersPath
import com.example.lunimary.util.myFollowingsPath
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

class UserDetailSourceImpl: BaseSourceImpl by BaseSourceImpl(),  UserDetailSource {
    override suspend fun likesOfUser(): DataResponse<Long> {
        return client.get(urlString = likesOfUserPath) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.init()
    }

    override suspend fun followings(): DataResponse<List<User>> {
        return client.get(
            urlString = myFollowingsPath,
        ) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.init()
    }

    override suspend fun followers(): DataResponse<List<User>> {
        return client.get(
            urlString = myFollowersPath
        ) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.init()
    }
}