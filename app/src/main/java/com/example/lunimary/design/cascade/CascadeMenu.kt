package com.example.lunimary.design.cascade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.DeleteSweep
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.FileCopy
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

val MAX_WIDTH = 192.dp

fun <T> AnimatedContentTransitionScope<T>.animateToPrevious(): ContentTransform {
    return slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) togetherWith
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
}

@ExperimentalAnimationApi
fun <T> AnimatedContentTransitionScope<T>.animateToNext(): ContentTransform {
    return slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) togetherWith
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
}

fun <T : Any> isNavigatingBack(
    currentMenu: CascadeMenuItem<T>,
    nextMenu: CascadeMenuItem<T>
): Boolean {
    return currentMenu.hasParent() && nextMenu == currentMenu.parent!!
}

@ExperimentalAnimationApi
@Composable
fun <T : Any> CascadeMenu(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    menu: CascadeMenuItem<T>,
    colors: CascadeMenuColors = cascadeMenuColors(),
    offset: DpOffset = DpOffset.Zero,
    width: Dp = MAX_WIDTH,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier
            .width(width)
            .background(colors.backgroundColor),
        expanded = isOpen,
        onDismissRequest = onDismiss,
        offset = offset
    ) {
        val state by remember { mutableStateOf(CascadeMenuState(menu)) }
        AnimatedContent(
            targetState = state.currentMenuItem,
            transitionSpec = {
                if (isNavigatingBack(initialState, targetState)) {
                    animateToPrevious()
                } else {
                    animateToNext()
                }
            }
        ) { targetMenu ->
            CascadeMenuContent(
                state = state,
                targetMenu = targetMenu,
                onItemSelected = onItemSelected,
                colors = colors,
            )
        }
    }
}

@Composable
fun <T : Any> CascadeMenuContent(
    state: CascadeMenuState<T>,
    targetMenu: CascadeMenuItem<T>,
    onItemSelected: (T) -> Unit,
    colors: CascadeMenuColors,
) {
    Column(modifier = Modifier.width(192.dp)) {
        if (targetMenu.hasParent()) {
            CascadeHeaderItem(
                targetMenu.title,
                colors.contentColor
            ) {
                state.currentMenuItem = targetMenu.parent!!
            }
        }
        if (targetMenu.hasChildren()) {
            for (item in targetMenu.children!!) {
                if (item.hasChildren()) {
                    CascadeParentItem(
                        item.id,
                        item.title,
                        item.icon,
                        colors.contentColor
                    ) { id ->
                        val child = targetMenu.getChild(id)
                        if (child != null) {
                            state.currentMenuItem = child
                        } else {
                            throw IllegalStateException("Invalid item id : $id")
                        }
                    }
                } else {
                    CascadeChildItem(
                        item.id,
                        item.title,
                        item.icon,
                        colors.contentColor,
                        onItemSelected
                    )
                }
            }
        }
    }
}

@Composable
fun Space() {
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun CascadeMenuItemIcon(icon: ImageVector, tint: Color) {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = icon,
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun CascadeMenuItemText(
    modifier: Modifier,
    text: String,
    color: Color,
    isHeaderText: Boolean = false,
) {
    val style =
        if (isHeaderText) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleSmall
    Text(
        modifier = modifier,
        text = text,
        style = style,
        color = color,
    )
}

@Composable
fun CascadeMenuItem(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    DropdownMenuItem(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        text = { Row(content = content, verticalAlignment = Alignment.CenterVertically) }
    )
}

@Composable
fun CascadeHeaderItem(
    title: String,
    contentColor: Color,
    onClick: () -> Unit,
) {
    CascadeMenuItem(onClick = { onClick() }) {
        CascadeMenuItemIcon(
            icon = Icons.AutoMirrored.Rounded.ArrowLeft,
            tint = contentColor.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        CascadeMenuItemText(
            modifier = Modifier.weight(1f),
            text = title,
            color = contentColor.copy(alpha = 0.8f),
            isHeaderText = true,
        )
    }
}

@Composable
fun <T> CascadeParentItem(
    id: T,
    title: String,
    icon: ImageVector?,
    contentColor: Color,
    onClick: (T) -> Unit,
) {
    CascadeMenuItem(onClick = { onClick(id) }) {
        if (icon != null) {
            CascadeMenuItemIcon(icon = icon, tint = contentColor)
            Space()
        }
        CascadeMenuItemText(
            modifier = Modifier.weight(1f),
            text = title,
            color = contentColor,
        )
        Space()
        CascadeMenuItemIcon(icon = Icons.Rounded.ArrowRight, tint = contentColor)
    }
}

@Composable
fun <T> CascadeChildItem(
    id: T,
    title: String,
    icon: ImageVector?,
    contentColor: Color,
    onClick: (T) -> Unit,
) {
    CascadeMenuItem(onClick = { onClick(id) }) {
        if (icon != null) {
            CascadeMenuItemIcon(icon = icon, tint = contentColor)
            Space()
        }
        CascadeMenuItemText(
            modifier = Modifier.weight(1f),
            text = title,
            color = contentColor,
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalAnimationApi
@Composable
@Preview
fun CascadeScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val channel = remember { Channel<String>(Channel.CONFLATED) }
    val (isOpen, setIsOpen) = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = channel) {
        channel.receiveAsFlow().collect {
            snackbarHostState.showSnackbar(it)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Box(contentAlignment = Alignment.TopEnd) {
                Menu(isOpen = isOpen, setIsOpen = setIsOpen, itemSelected = {
                    channel.trySend(it)
                    setIsOpen(false)
                })
                IconButton(
                    onClick = { setIsOpen(true) }) {
                    Icon(
                        imageVector = Icons.TwoTone.MoreVert,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Menu(isOpen: Boolean = false, setIsOpen: (Boolean) -> Unit, itemSelected: (String) -> Unit) {
    val menu = getMenu()
    CascadeMenu(
        isOpen = isOpen,
        menu = menu,
        onItemSelected = itemSelected,
        onDismiss = { setIsOpen(false) },
        offset = DpOffset(8.dp, 0.dp),
    )
}

fun getMenu(): CascadeMenuItem<String> {
    val menu = cascadeMenu {
        item("about", "About") {
            icon(Icons.TwoTone.Language)
        }
        item("copy", "Copy") {
            icon(Icons.TwoTone.FileCopy)
        }
        item("share", "Share") {
            icon(Icons.TwoTone.Share)
            item("to_clipboard", "To clipboard") {
                item("pdf", "PDF")
                item("epub", "EPUB")
                item("web_page", "Web page")
                item("microsoft_word", "Microsoft word")
            }
            item("as_a_file", "As a file") {
                item("pdf", "PDF")
                item("epub", "EPUB")
                item("web_page", "Web page")
                item("microsoft_word", "Microsoft word")
            }
        }
        item("remove", "Remove") {
            icon(Icons.TwoTone.DeleteSweep)
            item("yep", "Yep") {
                icon(Icons.TwoTone.Done)
            }
            item("go_back", "Go back") {
                icon(Icons.TwoTone.Close)
            }
        }
    }
    return menu
}
