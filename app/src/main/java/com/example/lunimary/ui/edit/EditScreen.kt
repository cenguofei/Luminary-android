package com.example.lunimary.ui.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.LightAndDarkPreview
import com.example.lunimary.design.theme.LunimaryTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditScreen(
    onBack: () -> Unit,
    onPublish: () -> Unit
) {
    val pagers = listOf("Edit", "Preview")
    val pagerState = rememberPagerState { pagers.size }
    val editViewModel: EditViewModel = viewModel()
    val coroutine = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Column {
            LunimaryToolbar(
                onBack = onBack,
                end = {
                    TextButton(onClick = onPublish) {
                        Text(
                            text = stringResource(id = R.string.publish),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
            Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray.copy(alpha = 0.5f))
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page ->
            if (page == EDIT_PAGE) {
                EditPage(
                    viewModel = editViewModel,
                    onPreviewClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(PREVIEW_PAGE)
                        }
                    }
                )
            } else {
                PreviewPage(
                    viewModel = editViewModel,
                    onEditClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(EDIT_PAGE)
                        }
                    }
                )
            }
        }
    }
}

private const val EDIT_PAGE = 0
private const val PREVIEW_PAGE = 1

@LightAndDarkPreview
@Composable
fun EditScreenPreview() {
    LunimaryTheme {
        LunimaryGradientBackground {
            EditScreen(
                onBack = {},
                onPublish = {}
            )
        }
    }
}