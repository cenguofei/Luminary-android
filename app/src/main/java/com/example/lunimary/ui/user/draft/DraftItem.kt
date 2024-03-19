package com.example.lunimary.ui.user.draft

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DraftItem(
    modifier: Modifier = Modifier,
    showDraftLabel: Boolean = false,
    canOperate: Boolean = false,
    articles: List<Article>,
    index: Int = 0,
    onClick: (Article) -> Unit,
    onItemSelected: (DraftItemOperations) -> Unit = {}
) {
    if (articles.isEmpty()) return
    val article = articles[index]
    Box(modifier = modifier) {
        ArticleItem(onItemClick = onClick, article =article)
        if (showDraftLabel) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 4.dp),
                shape = RoundedCornerShape(25)
            ) {
                Text(
                    text = stringResource(id = R.string.draft) + " ${articles.size}",
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        if (canOperate) {
            val (showDropdownMenu, setIsOpen) = remember { mutableStateOf(false) }
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { setIsOpen(!showDropdownMenu) },
                    modifier = Modifier.padding(end = 8.dp, top = 4.dp)
                ) {
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
                        onItemSelected(it as DraftItemOperations)
                    },
                    onDismiss = { setIsOpen(false) }
                )
            }
        }
    }
}

enum class DraftItemOperations {
    Remove
}