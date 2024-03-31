package com.example.lunimary.design

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    show: Boolean = true,
    description: String? = null
) {
    if (show) {
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            onDismissRequest = {}) {
            Surface(
                modifier = Modifier.size(height = 100.dp, width = 100.dp),
                shape = RoundedCornerShape(12),
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        LoadingWheel()
                        if (description != null) {
                            Text(
                                text = description,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
