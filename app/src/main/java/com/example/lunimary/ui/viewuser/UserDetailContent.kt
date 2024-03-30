package com.example.lunimary.ui.viewuser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.UserHeadImage
import com.example.lunimary.models.User
import com.example.lunimary.ui.LunimaryAppState

@Composable
fun ViewUserDetailContent(
    modifier: Modifier,
    user: User,
    viewModel: ViewUserViewModel,
    appState: LunimaryAppState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 120.dp)
    ) {
        ViewUserInformation(user = user)
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            shape = RoundedCornerShape(topStartPercent = 8, topEndPercent = 8),
        ) {
            ViewUserRoundedCornerContent(
                viewModel = viewModel,
                onItemClick = { appState.navToBrowse(it) }
            )
        }
    }
}

@Composable
fun ViewUserInformation(
    user: User
) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserHeadImage(model = user.realHeadUrl())
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = user.username,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID:${user.id + 5201314L}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}