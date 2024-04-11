package com.example.lunimary.design.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.theme.Blue40
import com.example.lunimary.design.theme.DarkGreen40
import com.example.lunimary.design.theme.Green40
import com.example.lunimary.design.theme.Orange40
import com.example.lunimary.design.theme.Purple40
import com.example.lunimary.design.theme.Red40
import com.example.lunimary.design.theme.Teal40
import com.example.lunimary.model.source.local.Tag

val tagColors: List<Color> get() = listOf(
    Blue40, DarkGreen40, Green40,
    Orange40, Purple40, Red40, Teal40
)
@Composable
fun Tag(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(vertical = 5.dp, horizontal = 8.dp),
    style: TextStyle = MaterialTheme.typography.labelSmall,
    tag: Tag
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = Color(tag.color),
        contentColor = Color.White
    ) {
        Text(
            text = tag.name,
            modifier = Modifier.padding(padding),
            style = style
        )
    }
}