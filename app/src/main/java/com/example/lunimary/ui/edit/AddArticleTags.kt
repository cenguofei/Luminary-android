package com.example.lunimary.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.Tag
import com.example.lunimary.models.source.local.Tag
import com.example.lunimary.util.empty

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddArticleTags(editViewModel: EditViewModel, historyTags: State<List<Tag>?>) {
    PublishSettingsItem(
        title = stringResource(id = R.string.add_label),
        modifier = Modifier.imePadding()
    ) {
        val tag = remember { mutableStateOf(empty) }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.width(200.dp)) {
                OutlinedTextField(
                    value = tag.value,
                    onValueChange = { tag.value = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.input_tag),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f)
                    ),
                )
            }
            TextButton(onClick = { editViewModel.addNewTag(tag.value) }) {
                Text(text = stringResource(id = R.string.add))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            editViewModel.tags.value.forEach {
                Tag(tag = it)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (historyTags.value?.isNotEmpty() == true) {
            val showMinus = remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.history_tags),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { showMinus.value = !showMinus.value },
                ) {
                    val textId = if (showMinus.value) R.string.finish else R.string.edit_cn
                    Text(text = stringResource(id = textId))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                historyTags.value?.forEach {
                    Box(contentAlignment = Alignment.Center) {
                        Tag(
                            tag = it,
                            modifier = Modifier.clickable(
                                onClick = {
                                    editViewModel.addHistoryTagToArticle(it)
                                },
                                role = Role.Tab
                            )
                        )
                        if (showMinus.value) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(15.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(start = 5.dp, bottom = 5.dp),
                                shape = RoundedCornerShape(50),
                                onClick = { editViewModel.removeHistoryTag(it) },
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Spacer(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(1.dp)
                                            .background(Color.White)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
