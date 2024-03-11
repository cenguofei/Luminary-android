package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.models.User

@Composable
fun UserDetailContent(
    modifier: Modifier,
    user: User,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit
) {
    val uiState = userDetailViewModel.uiState.observeAsState()
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 120.dp)) {
        UserInformation(user)
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.weight(1f).fillMaxSize(),
            shape = RoundedCornerShape(topStartPercent = 16, topEndPercent = 16),
        ) {
            RoundedCornerContent(
                uiState = uiState,
                userDetailViewModel = userDetailViewModel,
                onDraftClick = onDraftClick
            )
        }
    }
}

@Composable
fun UserInformation(user: User) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(50)
        ) {
            AsyncImage(
                model = user.headUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50)),
                placeholder = painterResource(id = R.drawable.head),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = user.username,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.id.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}