package com.example.lunimary.design.cascade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryIcons

val MAX_WIDTH = 192.dp

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

fun <T : Any> isNavigatingBack(
    currentMenu: CascadeMenuItem<T>,
    nextMenu: CascadeMenuItem<T>
): Boolean {
    return currentMenu.hasParent() && nextMenu == currentMenu.parent!!
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
private fun Space12() {
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
    CascadeMenuItem(onClick = onClick) {
        CascadeMenuItemIcon(
            icon = LunimaryIcons.ArrowLeft,
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
            Space12()
        }
        CascadeMenuItemText(
            modifier = Modifier.weight(1f),
            text = title,
            color = contentColor,
        )
        Space12()
        CascadeMenuItemIcon(icon = LunimaryIcons.ArrowRight, tint = contentColor)
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
            Space12()
        }
        CascadeMenuItemText(
            modifier = Modifier.weight(1f),
            text = title,
            color = contentColor,
        )
    }
}
