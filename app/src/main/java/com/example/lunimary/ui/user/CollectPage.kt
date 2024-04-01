package com.example.lunimary.ui.user

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor

@Composable
fun CollectPage(
    modifier: Modifier,
    onItemClick: (Article) -> Unit,
    collectsOfUser: LazyPagingItems<Article>
) {
    LunimaryPagingContent(
        modifier = modifier,
        items = collectsOfUser,
        key = { collectsOfUser[it]?.id!! }
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
