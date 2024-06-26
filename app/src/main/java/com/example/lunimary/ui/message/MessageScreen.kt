package com.example.lunimary.ui.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.lunimary.design.components.LBHorizontalDivider
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageScreen(
    modifier: Modifier,
    pagerState: PagerState,
    tabs: List<MessagePageType>,
    messageViewModel: MessageViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                divider = { },
            ) {
                tabs.forEachIndexed { index, tab ->
                    TextButton(
                        onClick = {
                            if (pagerState.currentPage != index) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = tab.pageName,
                            color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        LBHorizontalDivider()
        MessagePagers(
            pagerState = pagerState,
            tabs = tabs,
            messageViewModel = messageViewModel,
            onShowSnackbar = onShowSnackbar
        )
    }
}