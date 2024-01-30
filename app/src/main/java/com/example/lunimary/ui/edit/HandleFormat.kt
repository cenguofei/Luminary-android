package com.example.lunimary.ui.edit

import android.content.Context
import android.widget.EditText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.lunimary.R

val formatImages: List<ImageVector>
    @Composable get() = listOf(
        Icons.Default.FormatBold, Icons.Default.FormatItalic,
        Icons.Default.FormatStrikethrough, Icons.Default.FormatQuote,
        ImageVector.vectorResource(id = R.drawable.format_h1),
        ImageVector.vectorResource(id = R.drawable.format_h2),
        ImageVector.vectorResource(id = R.drawable.format_h3),
        ImageVector.vectorResource(id = R.drawable.format_h4),
    )
val formatRes: List<Int>
    get() = listOf(
        R.string.format_bold, R.string.format_italic,
        R.string.format_strikethrough, R.string.format_quote,
        R.string.format_h1, R.string.format_h2,
        R.string.format_h3, R.string.format_h4
    )

fun EditText.handleFormat(
    index: Int,
    context: Context
) {
    val format = context.getString(formatRes[index])
    if (index <= 2) {
        val newText = text.append(format)
        text = newText
        setSelection(newText.length - format.length / 2)
    } else {
        val selectionStart = selectionStart
        val layout = layout
        val lineNumber = layout.getLineForOffset(selectionStart)
        val lineStart = layout.getLineStart(lineNumber)
        editableText.insert(lineStart, format.plus(" "))
    }
}