package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.DataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDetailRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDetailSource, CollectSource {
    private val userDetailSource: UserDetailSource = UserDetailSourceImpl()
    private val collectSource: CollectSource = CollectSourceImpl()

    override suspend fun likesOfUser(): DataResponse<Long> =
        withContext(dispatcher) { userDetailSource.likesOfUser() }

    override suspend fun followings(): DataResponse<List<User>> =
        withContext(dispatcher) { userDetailSource.followings() }

    override suspend fun followers(): DataResponse<List<User>> =
        withContext(dispatcher) { userDetailSource.followers() }

    override suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>> =
        withContext(dispatcher) { collectSource.collectsOfUser(userId) }
}