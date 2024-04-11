package com.example.lunimary.ui.search


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.R
import com.example.lunimary.design.components.BackButton
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.search.pages.ArticlePage
import com.example.lunimary.ui.search.pages.UserPage
import com.example.lunimary.util.empty
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreenRoute(
    appState: LunimaryAppState,
    onBack: () -> Unit,
) {
    val tabs = listOf(SearchType.Article, SearchType.User)
    val pagerState = rememberPagerState(0) { tabs.size }
    val coroutineScope = rememberCoroutineScope()
    val searchViewModel: SearchViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()) {
            val primaryColor = MaterialTheme.colorScheme.primary
            BackButton(tint = primaryColor, onClick = onBack)
            val searchContentState =
                searchViewModel.searchContent.collectAsStateWithLifecycle(empty)
            val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            OutlinedTextField(
                value = searchContentState.value,
                onValueChange = searchViewModel::onSearchQueryChanged,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                placeholder = {
                    val text =
                        if (tabs[pagerState.currentPage] == SearchType.Article) stringResource(
                            id = R.string.search_article
                        ) else stringResource(id = R.string.search_user)
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    focusedBorderColor = borderColor,
                )
            )
            Spacer(modifier = Modifier.width(25.dp))
        }
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier,
            containerColor = Color.Transparent,
            divider = {}
        ) {
            tabs.forEachIndexed { index, tab ->
                TextButton(
                    onClick = {
                        searchViewModel.onSearchTypeChanged(tab)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = tab.tabName,
                        color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }

        val articleItems = searchViewModel.searchArticles.collectAsLazyPagingItems()
        val userItems = searchViewModel.searchUsers.collectAsLazyPagingItems()
        DisposableEffect(
            key1 = Unit,
            effect = {
                searchViewModel.registerOnHaveNetwork("ArticlePage") {
                    articleItems.retry()
                }
                searchViewModel.registerOnHaveNetwork("UserPage") {
                    userItems.retry()
                }
                onDispose {
                    searchViewModel.unregisterOnHaveNetwork("ArticlePage")
                    searchViewModel.unregisterOnHaveNetwork("UserPage")
                }
            }
        )
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) {
            when(tabs[it]) {
                SearchType.Article -> {
                    searchViewModel.onSearchTypeChanged(SearchType.Article)
                    ArticlePage(
                        onItemClick = { a -> appState.navToBrowse(a) },
                        articleItems = articleItems,
                    )
                }
                SearchType.User -> {
                    searchViewModel.onSearchTypeChanged(SearchType.User)
                    UserPage(
                        userItems = userItems,
                        onItemClick = { u -> appState.navToViewUser(u, Screens.Search.route) },
                    )
                }
            }
        }
    }
}