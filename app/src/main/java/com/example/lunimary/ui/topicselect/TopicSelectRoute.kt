package com.example.lunimary.ui.topicselect

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.nicepage.LunimaryStateContent
import com.example.lunimary.design.nicepage.StateContentData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopicSelectRoute(
    onBack: () -> Unit,
    coroutineScope: CoroutineScope
) {
    val viewModel: TopicSelectViewModel = viewModel()
    DisposableEffect(
        key1 = Unit,
        effect = {
            onDispose {
                coroutineScope.launch {
                    viewModel.createOrUpdate(coroutineScope)
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                state = rememberScrollableState(consumeScrollDelta = { it }),
                orientation = Orientation.Vertical
            ),
    ) {
        LunimaryToolbar(
            onBack = onBack,
            between = {
                Text(
                    text = stringResource(id = R.string.topic_setting),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            modifier = Modifier.statusBarsPadding()
        )
        val uiState = viewModel.uiState
        LunimaryStateContent(
            stateContentData = StateContentData(
                isError = uiState is NetworkResult.Error,
                showShimmer = uiState is NetworkResult.Loading
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            val data = (uiState as? NetworkResult.Success)?.data ?: return@LunimaryStateContent
            LocalSelected(
                viewModel = viewModel,
                userSelectedTopics = data.selectedTopicsState
            )
            RecommendTopic(
                viewModel = viewModel,
                recommendTopics = data.recommendTopics
            )
        }
    }
}



