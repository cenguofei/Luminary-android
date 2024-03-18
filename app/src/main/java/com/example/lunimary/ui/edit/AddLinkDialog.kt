package com.example.lunimary.ui.edit

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lunimary.R
import com.example.lunimary.design.LinearButton
import com.example.lunimary.util.empty

@Composable
fun AddLinkDialog(
    showDialog: MutableState<Boolean>,
    onFinish: (name: String, link: String) -> Unit
) {
    if (showDialog.value) {
        var name by remember { mutableStateOf(empty) }
        var link by remember { mutableStateOf(empty) }
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                modifier = Modifier.size(width = 280.dp, height = 200.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    val outlineModifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    val textStyle = MaterialTheme.typography.labelLarge

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = outlineModifier,
                        label = {
                            Text(
                                text = stringResource(R.string.link_name),
                                style = textStyle
                            )
                        },
                        shape = RoundedCornerShape(16),
                        singleLine = true,
                        textStyle = textStyle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = link,
                        onValueChange = { link = it },
                        modifier = outlineModifier,
                        label = {
                            Text(
                                text = stringResource(R.string.link_address),
                                style = textStyle
                            )
                        },
                        shape = RoundedCornerShape(16),
                        singleLine = true,
                        textStyle = textStyle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        LinearButton(
                            onClick = { showDialog.value = false },
                            text = stringResource(id = R.string.cancel),
                            textStyle = MaterialTheme.typography.labelMedium,
                            height = 25.dp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        LinearButton(
                            onClick = {
                                showDialog.value = false
                                onFinish(name, link)
                            },
                            text = stringResource(id = R.string.finish),
                            textStyle = MaterialTheme.typography.labelMedium,
                            height = 25.dp
                        )
                    }
                }
            }
        }
    }
}