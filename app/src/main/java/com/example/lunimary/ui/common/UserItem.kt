package com.example.lunimary.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.design.UserHeadImage
import com.example.lunimary.models.User

@Composable
fun UserItem(
    user: User,
    clickEnabled: Boolean = true,
    onMoreClick: () -> Unit = {},
    onItemClick: (User) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = androidx.compose.material.ripple.rememberRipple(),
                enabled = clickEnabled,
                onClick = { onItemClick(user) }
            ),
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
        IconButton(onClick = onMoreClick) {
            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null)
        }
    }
}

val signatures = listOf(
    "每天都要开心",
    "世界很大，我要去走走",
    "我真的栓q",
    "夜未央，天微亮",
    "又是一个安静的晚上",
    "炊烟袅袅升起",
    "如传世的青花瓷，自顾自美丽，你眼带笑意"
)
