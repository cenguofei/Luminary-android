package com.example.lunimary.design

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.theme.Blue40
import com.example.lunimary.design.theme.Blue80
import com.example.lunimary.design.theme.DarkGreen40
import com.example.lunimary.design.theme.Green40
import com.example.lunimary.design.theme.Orange40
import com.example.lunimary.design.theme.Purple40
import com.example.lunimary.design.theme.Red40
import com.example.lunimary.design.theme.Teal40

val tagColors: List<Color> @Composable get() = listOf(
    MaterialTheme.colorScheme.primary,
    Blue40, DarkGreen40, Green40,
    Orange40, Purple40, Red40, Teal40
)
@Composable
fun Tag(
    modifier: Modifier = Modifier,
    color: Color = tagColors.random(),
    name: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8),
        color = color,
        contentColor = Color.White
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}