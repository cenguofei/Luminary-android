package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LikeRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LikeSource {
    private val likeSource: LikeSource = LikeSourceImpl()

    override suspend fun likesOfUser(userId: Long): DataResponse<List<Article>> =
        withContext(dispatcher) { likeSource.likesOfUser(userId) }
}