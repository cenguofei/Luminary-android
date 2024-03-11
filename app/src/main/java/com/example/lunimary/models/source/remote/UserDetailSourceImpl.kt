package com.example.lunimary.models.source.remote

import com.example.lunimary.models.User
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.likesOfUserPath
import com.example.lunimary.util.myFollowersPath
import com.example.lunimary.util.myFollowingsPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

class UserDetailSourceImpl(private val client: HttpClient = httpClient) : UserDetailSource {
    override suspend fun likesOfUser(): DataResponse<Long> {
        return client.get(urlString = likesOfUserPath) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.let { it.body<DataResponse<Long>>().init(it) }
    }

    override suspend fun followings(): DataResponse<List<User>> {
        return client.get(
            urlString = myFollowingsPath,
        ) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.let { it.body<DataResponse<List<User>>>().init(it) }
    }

    override suspend fun followers(): DataResponse<List<User>> {
        return client.get(
            urlString = myFollowersPath
        ) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.let { it.body<DataResponse<List<User>>>().init(it) }
    }
}