package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.Article
import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.CollectedArticlesOfUser
import com.example.lunimary.models.source.remote.UserDetailSource

class UserDetailRepository : Repository by Repository() {
    private val userDetailSource = UserDetailSource
    private val collectSource = CollectedArticlesOfUser

    suspend fun likesOfUser(userId: Long): DataResponse<Long> =
        withDispatcher { userDetailSource.likesOfUser(userId) }

    suspend fun followings(userId: Long): RelationResponse<FollowInfo> =
        withDispatcher { userDetailSource.followings(userId = userId) }

    suspend fun followers(userId: Long): RelationResponse<FollowersInfo> =
        withDispatcher { userDetailSource.followers(userId = userId) }

    suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>> =
        withDispatcher { collectSource.collectsOfUser(userId) }

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend> =
        withDispatcher { userDetailSource.mutualFollowUsers() }
}