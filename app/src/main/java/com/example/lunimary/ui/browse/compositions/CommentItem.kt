package com.example.lunimary.ui.browse.compositions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.design.components.UserHeadImage
import com.example.lunimary.model.Comment
import com.example.lunimary.model.User

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    commentOwner: User
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        UserHeadImage(
            model = commentOwner.realHeadUrl(),
            size = 40.dp,
            modifier = Modifier.align(Alignment.Top).padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = commentOwner.username,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = comment.niceDate,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = comment.content,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}