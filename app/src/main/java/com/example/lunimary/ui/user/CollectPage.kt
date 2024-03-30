package com.example.lunimary.ui.user

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor

@Composable
fun CollectPage(
    userDetailViewModel: UserDetailViewModel,
    modifier: Modifier,
    onItemClick: (Article) -> Unit
) {
    val collectsOfUser = userDetailViewModel.collectsOfUser.collectAsLazyPagingItems()
    LunimaryPagingContent(
        modifier = modifier,
        items = collectsOfUser,
        key = { collectsOfUser[it]?.id!! },
        viewModel = userDetailViewModel,
        pagingKey = "CollectPage_UserDetailViewModel"
    ) {
        ArticleItem(
            onItemClick = onItemClick,
            article = it,
            containerColor = ArticleItemContainerColor.Default.copy(
                normalColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
            )
        )
    }
}
