package com.example.lunimary.ui.relation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.design.UserHeadImage
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.user.draft.DraftItemOperations
import com.example.lunimary.util.empty

data class FollowItemData(
    val followInfo: FollowInfo,
    val cancelFollow: Boolean = false
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FollowItem(
    followInfoData: FollowItemData,
    onMoreClick: () -> Unit,
    onFollowClick: () -> Unit,
    onCancelFollowClick: () -> Unit,
    state: MutableState<NetworkResult<Unit>>
) {
    val followInfoState = remember { mutableStateOf(followInfoData) }
    val user = followInfoState.value.followInfo.myFollow
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserHeadImage(model = user.realHeadUrl(), size = 50.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            val sig = remember { signatures.random() }
            Text(
                text = sig,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        val enabled = remember { mutableStateOf(true) }
        when (state.value) {
            is NetworkResult.Loading -> {
                enabled.value = false
            }

            is NetworkResult.Success -> {
                enabled.value = true
                LaunchedEffect(
                    key1 = Unit,
                    block = {
                        followInfoState.value = followInfoState.value.copy(
                            cancelFollow = !followInfoState.value.cancelFollow
                        )
                    }
                )
            }

            else -> {
                enabled.value = true
            }
        }
        val (showDropdownMenu, setIsOpen) = remember { mutableStateOf(false) }
        Box {
            val cancelFollow = followInfoState.value.cancelFollow
            Surface(
                color = if (cancelFollow) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8),
                onClick = {
                    when {
                        cancelFollow -> {
                            onFollowClick()
                        }

                        followInfoState.value.followInfo.alsoFollowMe -> {
                            setIsOpen(true)
                        }

                        else -> {
                            //已关注
                            onCancelFollowClick()
                        }
                    }
                },
                enabled = enabled.value
            ) {
                Text(
                    text = when {
                        cancelFollow -> stringResource(id = R.string.follow)
                        followInfoState.value.followInfo.alsoFollowMe -> stringResource(id = R.string.mutual_follow)
                        else -> stringResource(id = R.string.already_follow)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 10.sp,
                    color = if (cancelFollow) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.W500
                )
            }
            CascadeMenu(
                isOpen = showDropdownMenu,
                menu = cascadeMenu {
                    item(id = "cancel_follow", title = "取消关注") {
                        icon(Icons.Default.Close)
                    }
                },
                onItemSelected = {
                    setIsOpen(false)
                    if (it == "cancel_follow") {
                        onCancelFollowClick()
                    }
                },
                onDismiss = { setIsOpen(false) },
                width = 150.dp
            )
        }

        IconButton(onClick = onMoreClick) {
            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null)
        }
    }
}