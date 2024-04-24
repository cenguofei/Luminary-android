package com.example.lunimary.design.nicepage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.util.empty

@Composable
fun ShowReasonForNoContent(
    modifier: Modifier = Modifier,
    description: String = empty,
    @DrawableRes id: Int,
    refreshEnabled: Boolean = true,
    onRefreshClick: () -> Unit = {},
    retryEnabled: Boolean = false,
    onRetryClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.size(height = 300.dp, width = 200.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id), contentDescription = null)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            if (refreshEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onRefreshClick) {
                    Text(
                        text = stringResource(id = R.string.click_refresh),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (retryEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onRetryClick) {
                    Text(
                        text = stringResource(id = R.string.click_retry),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}