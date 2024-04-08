package com.example.lunimary.ui.edit.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.ShowSnackbar
import com.example.lunimary.models.source.local.Tag
import com.example.lunimary.ui.edit.EditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomSheetContent(
    reallyPublish: () -> Unit,
    editViewModel: EditViewModel,
    coroutineScope: CoroutineScope,
    historyTags: State<List<Tag>?>,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.publish_setting),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 9.dp)) {
            item {
                AddArticleTags(editViewModel = editViewModel, historyTags = historyTags)
            }
            item {
                ChooseVisibleMode(editViewModel = editViewModel)
            }
            item {
                ArticleCover(editViewModel = editViewModel, coroutineScope = coroutineScope)
            }
        }
        BottomButtons(
            editViewModel = editViewModel,
            reallyPublish = reallyPublish,
            coroutineScope = coroutineScope,
        )
    }
}

@Composable
private fun BottomButtons(
    editViewModel: EditViewModel,
    reallyPublish: () -> Unit,
    coroutineScope: CoroutineScope,
) {
    val showSnackbar = remember { mutableStateOf(false) }
    if (showSnackbar.value) {
        ShowSnackbar(message = stringResource(id = R.string.auto_save_as_draft))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LinearButton(
            modifier = Modifier.weight(1f),
            onClick = { editViewModel.saveAsDraft() },
            text = stringResource(id = R.string.save_as_draft),
            height = 40.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
        LinearButton(
            modifier = Modifier.weight(1f),
            onClick = {
                editViewModel.checkArticleParams(
                    success = reallyPublish,
                    problem = {
                        coroutineScope.launch {
                            snackbarHostState?.showSnackbar(message = it)
                        }
                    }
                )
            },
            text = stringResource(id = R.string.publish_content),
            height = 40.dp
        )
    }
}