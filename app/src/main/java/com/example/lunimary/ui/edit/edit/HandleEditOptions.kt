package com.example.lunimary.ui.edit.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

val editOptions: List<ImageVector>
    @Composable get() = listOf(
        Icons.Default.Image,
        //ImageVector.vectorResource(id = R.drawable.divider),
        Icons.Default.AddLink,
        //Icons.Default.Undo,
        //Icons.Default.Redo,
        //Icons.Default.Settings
    )