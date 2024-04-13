package com.example.lunimary.ui.message.pages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.CascadeMenuItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = { setIsOpen(true) }
                ),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)) {
                Box(modifier = Modifier) {
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
    }
}