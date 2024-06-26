package com.example.lunimary.ui.edit.edit

import android.view.View
import android.widget.EditText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.example.lunimary.ui.edit.EditViewModel
import dev.jeziellago.compose.markdowntext.applyFontSize

@Composable
fun AndroidTitleView(
    titleView: View,
    titleEditView: EditText,
    viewModel: EditViewModel
) {
    val titleColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val textStyle = MaterialTheme.typography.headlineLarge
    Row(modifier = Modifier.padding(vertical = 16.dp)) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                titleEditView.apply {
                    setTextColor(titleColor)
                    setHintTextColor(titleColor)
                    requestFocus()
                    applyFontSize(textStyle)
                    addTextChangedListener(
                        afterTextChanged = { viewModel.afterTitleChanged(it.toString()) }
                    )
                }
                titleView
            }
        )
    }
}

@Composable
fun AndroidBodyView(
    bodyView: View,
    bodyEditText: EditText,
    viewModel: EditViewModel,
    modifier: Modifier
) {
    val bodyColor = MaterialTheme.colorScheme.onSurface.toArgb()
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            bodyEditText.apply {
                setTextColor(bodyColor)
                setHintTextColor(bodyColor)
                addTextChangedListener(
                    afterTextChanged = {
                       viewModel.afterBodyChanged(it.toString())
                    }
                )
            }
            bodyView
        }
    )
}