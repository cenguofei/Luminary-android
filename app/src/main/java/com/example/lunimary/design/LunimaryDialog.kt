package com.example.lunimary.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.lunimary.R
import com.example.lunimary.util.empty

@Composable
fun LunimaryDialog(
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    text: String,
    height: Dp = 100.dp,
    width: Dp = 250.dp,
    openDialog: MutableState<Boolean> = remember { mutableStateOf(false) },
    onCancelClick: (() -> Unit)? = null,
    onConfirmClick: (() -> Unit)? = null,
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
                onDismissRequest()
            },
            properties = properties
        ) {
            Surface(
                modifier = Modifier.size(height = height, width = width),
                shape = RoundedCornerShape(12),
            ) {
                LunimaryBackground {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(modifier = Modifier) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                        onCancelClick?.let { it() }
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                                Spacer(modifier = Modifier.width(25.dp))
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                        onConfirmClick?.let { it() }
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.ok))
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}