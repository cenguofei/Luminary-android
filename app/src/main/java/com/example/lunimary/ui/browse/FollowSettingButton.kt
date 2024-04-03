package com.example.lunimary.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.lunimary.R
import com.example.lunimary.base.currentUser
import com.example.lunimary.design.SmallButton
import com.example.lunimary.models.User


@Composable
fun FollowSettingButton(
    owner: User,
    hasFetchedFriendship: Boolean,
    isFollowByMe: Boolean,
    onUnfollowClick: () -> Unit,
    onFollowClick: () -> Unit,
) {
    if (currentUser.username != owner.username && hasFetchedFriendship) {
        if (isFollowByMe) {
            SmallButton(
                onClick = onUnfollowClick,
                text = stringResource(id = R.string.unfollow),
                modifier = Modifier,
            )
        } else {
            SmallButton(
                onClick = onFollowClick,
                text = stringResource(id = R.string.follow),
                modifier = Modifier,
            )
        }
    }
}