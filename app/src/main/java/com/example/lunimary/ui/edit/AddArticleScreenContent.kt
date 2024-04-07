package com.example.lunimary.ui.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.lunimary.R
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.design.LunimaryToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddArticleScreenContent(
    onBack: () -> Unit,
    onPublish: () -> Unit,
    editViewModel: EditViewModel,
    coroutineScope: CoroutineScope,
    onNavToWeb: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val pagers = listOf(stringResource(id = R.string.edit), stringResource(id = R.string.preview))
    val pagerState = rememberPagerState { pagers.size }
    val coroutine = rememberCoroutineScope()
    val showWarnDialog = remember { mutableStateOf(false) }
    LunimaryDialog(
        text = stringResource(id = R.string.title_or_content_cannot_empty),
        openDialog = showWarnDialog
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Column {
            LunimaryToolbar(
                onBack = onBack,
                end = {
                    TextButton(
                        onClick = {
                            if(editViewModel.canPublish()) {
                                onPublish()
                            } else {
                                showWarnDialog.value = true
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.publish),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Gray.copy(alpha = 0.5f))
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
                    },
                    coroutineScope = coroutineScope,
                    onNavToWeb = onNavToWeb,
                    onShowMessage = onShowMessage
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