package com.example.lunimary.ui.message.pages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.MarginSurfaceItem
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.CascadeMenuItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T : Any> MessagePage(
    items: LazyPagingItems<PageItem<T>>,
    menu: CascadeMenuItem<String>,
    onItemSelected: (String, PageItem<T>) -> Unit,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable BoxScope.(T) -> Unit
) {
    LunimaryPagingContent(
        items = items,
        key = key
    ) { _, item ->
        val (showDropdownMenu, setIsOpen) = remember { mutableStateOf(false) }
        MarginSurfaceItem(
            onLongClick = { setIsOpen(true) }
        ) {
            itemContent(item.data)
            CascadeMenu(
                isOpen = showDropdownMenu,
                menu = menu,
                onItemSelected = {
                    setIsOpen(false)
                    onItemSelected(it, item)
                },
                onDismiss = { setIsOpen(false) },
                width = 150.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}