package com.example.lunimary.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val icon: ImageVector,
    val route: String,
) {
    Home(
        icon = Icons.Default.Cottage,
        route = "$TOP_ROOT/{topScreen}"
    ),
    Message(
        icon = Icons.AutoMirrored.Filled.Chat,
        route = "$TOP_ROOT/{topScreen}"
    ),
    User(
        icon = Icons.Default.Person,
        route = "$TOP_ROOT/{topScreen}"
    );

    @Composable
    fun tintColor(selectedBottomTab: TopLevelDestination): Color {
        return if (selectedBottomTab == this) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        }
    }
}

const val TOP_ROOT = "screens_root"
const val HOME_ROOT = "home"
const val MESSAGE_ROOT = "message"
const val USER_ROOT = "user"

const val HOME_ROUTE = "$TOP_ROOT/$HOME_ROOT"
const val MESSAGE_ROUTE = "$TOP_ROOT/$MESSAGE_ROOT"
const val USER_ROUTE = "$TOP_ROOT/$USER_ROOT"

