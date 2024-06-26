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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.components.LinearButton
import com.example.lunimary.model.Article
import com.example.lunimary.model.source.local.Tag
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.ui.edit.EditViewModel

@Composable
fun BottomSheetContent(
    reallyPublish: () -> Unit,
    editViewModel: EditViewModel,
    historyTags: State<List<Tag>?>,
    onUpdateSuccess: (updated: Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onDismissSheet: () -> Unit,
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            item {
                ArticleTags(editViewModel = editViewModel, historyTags = historyTags)
            }
            item {
                ChooseVisibleMode(editViewModel = editViewModel)
            }
            item {
                ArticleCover(editViewModel = editViewModel, onShowSnackbar = onShowSnackbar)
            }
        }
        BottomButtons(
            editViewModel = editViewModel,
            reallyPublish = reallyPublish,
            onUpdateSuccess = onUpdateSuccess,
            onShowSnackbar = onShowSnackbar,
            onDismissSheet = onDismissSheet
        )
    }
}

@Composable
private fun BottomButtons(
    editViewModel: EditViewModel,
    reallyPublish: () -> Unit,
    onUpdateSuccess: (updated: Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onDismissSheet: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val uiState = editViewModel.uiState
        when (uiState.editType) {
            EditType.Edit -> {
                if (uiState.theArticleChanged()) {
                    LinearButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            editViewModel.updateRemoteArticle(
                                onUpdateSuccess = {
                                    onUpdateSuccess(it)
                                },
                                notSatisfy = { onDismissSheet() ; onShowSnackbar(it, null) }
                            )
                        },
                        text = stringResource(id = R.string.update_content),
                        height = 40.dp
                    )
                }
            }

            EditType.Draft -> {
                if (uiState.theArticleChanged()) {
                    LinearButton(
                        modifier = Modifier.weight(1f),
                        onClick = { editViewModel.updateDraft() },
                        text = stringResource(id = R.string.updated_draft),
                        height = 40.dp
                    )
                    if (uiState.canPublish) {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                if (uiState.canPublish) {
                    LinearButton(
                        modifier = Modifier.weight(1f),
                        onClick = reallyPublish,
                        text = stringResource(id = R.string.publish_content),
                        height = 40.dp
                    )
                }
            }

            EditType.New -> {
                if (uiState.canSaveAsDraft) {
                    LinearButton(
                        modifier = Modifier.weight(1f),
                        onClick = { editViewModel.saveAsDraft() },
                        text = stringResource(id = R.string.save_as_draft),
                        height = 40.dp
                    )
                    if (uiState.canPublish) {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                if (uiState.canPublish) {
                    LinearButton(
                        modifier = Modifier.weight(1f),
                        onClick = reallyPublish,
                        text = stringResource(id = R.string.publish_content),
                        height = 40.dp
                    )
                }
            }
        }
    }
}