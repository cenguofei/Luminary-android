package com.example.lunimary.ui.browse

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.base.currentUser
import com.example.lunimary.design.Button
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
            Button(
                onClick = onUnfollowClick,
                text = stringResource(id = R.string.unfollow),
                modifier = Modifier
                    .width(45.dp)
                    .height(20.dp),
                textStyle = MaterialTheme.typography.bodySmall,
            )
        } else {
            Button(
                onClick = onFollowClick,
                text = stringResource(id = R.string.follow),
                modifier = Modifier
                    .width(45.dp)
                    .height(20.dp),
                textStyle = MaterialTheme.typography.bodySmall,
            )
        }
    }
}