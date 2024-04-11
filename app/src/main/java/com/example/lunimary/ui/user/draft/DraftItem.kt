package com.example.lunimary.ui.user.draft

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.model.Article
import com.example.lunimary.ui.common.ArticleItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DraftItem(
    modifier: Modifier = Modifier,
    showDraftLabel: Boolean = false,
    canOperate: Boolean = false,
    article: Article,
    draftsNum: Int,
    onClick: (Article) -> Unit,
    onItemSelected: (DraftItemOperations, Article) -> Unit = { _, _ -> }
) {
    @Composable
    fun TopEndOptions() {
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
                item(id = DraftItemOperations.Remove, title = "删除") {
                    icon(Icons.Default.Delete)
                }
            },
            onItemSelected = {
                setIsOpen(false)
                onItemSelected(it as DraftItemOperations, article)
            },
            onDismiss = { setIsOpen(false) }
        )
    }

    Box(modifier = modifier) {
        ArticleItem(
            onItemClick = { onClick(it.data) },
            articlePageItem = PageItem(article),
            showAboutArticle = false,
            topEndOptions = if (!canOperate) null else {
                { TopEndOptions() }
            }
        )
        if (showDraftLabel) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp),
                shape = RoundedCornerShape(25)
            ) {
                Text(
                    text = stringResource(id = R.string.draft) + "$draftsNum",
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    fontSize = 12.sp
                )
            }
        }
    }
}

enum class DraftItemOperations {
    Remove
}