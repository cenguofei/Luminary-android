package com.example.lunimary.ui.viewuser

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor
import java.util.UUID


@Composable
fun UserArticles(
    viewModel: ViewUserViewModel,
    onItemClick: (Article) -> Unit,
    modifier: Modifier
) {
    val userArticles = viewModel.userArticles.collectAsLazyPagingItems()
    LunimaryPagingContent(
        modifier = modifier,
        items = userArticles,
        key = { userArticles[it]?.id ?: UUID.randomUUID() },
        viewModel = viewModel,
        pagingKey = "UserArticles_ViewUserViewModel"
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
