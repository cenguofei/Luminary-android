package com.example.lunimary.ui.user

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.local.articleDao
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.user.draft.DraftItem


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PublicPage(
    onItemClick: (PageItem<Article>) -> Unit,
    modifier: Modifier,
    onDraftClick: () -> Unit,
    compositionsState: LazyPagingItems<PageItem<Article>>,
    navToEdit: (EditType, Article) -> Unit
) {
    val drafts = articleDao.findArticlesByUsername(currentUser.username).observeAsState()
    LunimaryPagingContent(
        modifier = modifier,
        items = compositionsState,
        key = { compositionsState[it]?.data?.id!! },
        topItem = {
            if (drafts.value?.isNotEmpty() == true) {
                DraftItem(
                    articles = drafts.value ?: emptyList(),
                    onClick = { onDraftClick() },
                    showDraftLabel = true
                )
            }
        }
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            articlePageItem = item,
            topEndOptions = {
                val (showDropdownMenu, setIsOpen) = remember { mutableStateOf(false) }
                IconButton(onClick = { setIsOpen(!showDropdownMenu) },) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = null
                    )
                }
                CascadeMenu(
                    isOpen = showDropdownMenu,
                    menu = cascadeMenu {
                        item(id = "edit", title = "±à¼­") {
                            icon(Icons.Default.Edit)
                        }
                    },
                    onItemSelected = {
                        setIsOpen(false)
                        if (it == "edit") { navToEdit(EditType.Edit, item.data) }
                    },
                    onDismiss = { setIsOpen(false) }
                )
            }
        )
    }
}
