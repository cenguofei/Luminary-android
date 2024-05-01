package com.example.lunimary.design.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.lunimary.design.LunimaryIcons

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = LunimaryIcons.ArrowBack,
            contentDescription = null,
            tint = tint
        )
    }
}