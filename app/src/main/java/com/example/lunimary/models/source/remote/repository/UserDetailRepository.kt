package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.CollectedArticlesOfUser
import com.example.lunimary.models.source.remote.UserDetailSource
import com.example.lunimary.models.source.remote.impl.CollectedArticlesOfUserImpl
import com.example.lunimary.models.source.remote.impl.UserDetailSourceImpl

class UserDetailRepository : BaseRepository by BaseRepository(), UserDetailSource, CollectedArticlesOfUser {
    private val userDetailSource: UserDetailSource = UserDetailSourceImpl()
    private val collectSource: CollectedArticlesOfUser = CollectedArticlesOfUserImpl()

    override suspend fun likesOfUser(): DataResponse<Long> =
        withDispatcher { userDetailSource.likesOfUser() }

    override suspend fun followings(): RelationResponse<FollowInfo> =
        withDispatcher { userDetailSource.followings() }

    override suspend fun followers(): RelationResponse<FollowersInfo> =
        withDispatcher { userDetailSource.followers() }

    override suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>> =
        withDispatcher { collectSource.collectsOfUser(userId) }

    override suspend fun mutualFollowUsers(): RelationResponse<UserFriend> =
        withDispatcher { userDetailSource.mutualFollowUsers() }
}