package com.example.lunimary.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ArticleItemContainerColor(
    val visitedColor: Color,
    val normalColor: Color
) {
    companion object {
        val Default: ArticleItemContainerColor
            @Composable get() = ArticleItemContainerColor(
                visitedColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                normalColor = /*if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.15f
                ) else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)*/ MaterialTheme.colorScheme.surface
            )
    }
}