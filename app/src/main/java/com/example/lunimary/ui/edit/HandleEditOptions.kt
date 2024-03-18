package com.example.lunimary.ui.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.lunimary.R

val editOptions: List<ImageVector>
    @Composable get() = listOf(
        Icons.Default.Image,
        //ImageVector.vectorResource(id = R.drawable.divider),
        Icons.Default.AddLink,
        //Icons.Default.Undo,
        //Icons.Default.Redo,
        //Icons.Default.Settings
    )

fun handleEditOptions(index: Int) {

}