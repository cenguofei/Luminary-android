package com.example.lunimary.ui.edit.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector

enum class EditOption(
    val icon: ImageVector
) {
    Image(Icons.Default.Image),
    AddLink(Icons.Default.AddLink),
    Delete(Icons.Default.Delete)
}