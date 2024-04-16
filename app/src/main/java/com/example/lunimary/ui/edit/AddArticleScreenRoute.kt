package com.example.lunimary.ui.edit

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.design.myObserveAsState
import com.example.lunimary.model.Article
import com.example.lunimary.ui.edit.bottomsheet.BottomSheetContent
import com.example.lunimary.util.unknownErrorMsg


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArticleScreenRoute(
    onPublish: () -> Unit,
    editViewModel: EditViewModel,
    onFinish: () -> Unit,
    onNavToWeb: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onPublishedArticleDelete: () -> Unit,
    editType: EditType,
    theArticle: PageItem<Article>?,
    onBack: () -> Unit
) {
    FillArticleEffect(
        article = theArticle?.data,
        editViewModel = editViewModel,
        editType = editType
    )
    val saveMessage = stringResource(id = R.string.auto_save_as_draft)
    val updateMessage = stringResource(id = R.string.has_updated_draft)
    val openDialog = remember { mutableStateOf(false) }
    fun onBackAction() {
        when (editType) {
            EditType.New -> {
                if (editViewModel.uiState.canSaveAsDraft && editViewModel.shouldSaveAsDraft) {
                    if (editViewModel.saveAsDraftEnabled) {
                        editViewModel.saveAsDraft()
                        onShowSnackbar(saveMessage, null)
                    }
                }
                onBack()
            }

            EditType.Draft -> {
                if (editViewModel.uiState.theArticleChanged() && editViewModel.shouldUpdateDraft) {
                    onShowSnackbar(updateMessage, null)
                    editViewModel.updateDraft()
                }
                onBack()
            }

            EditType.Edit -> {
                if (editViewModel.uiState.theArticleChanged()) {
                    openDialog.value = true
                } else {
                    onBack()
                }
            }
        }
    }
    LunimaryDialog(
        text = stringResource(id = R.string.update_of_remote_not_saved),
        openDialog = openDialog,
        onConfirmClick = {
            editViewModel.updateRemoteArticle(
                onUpdateSuccess = { onUpdateSuccess(it, theArticle) },
                notSatisfy = { onShowSnackbar(it, null) }
            )
        },
        onCancelClick = onBack
    )
    val updateArticleState = editViewModel.updateArticleState.collectAsStateWithLifecycle()
    when (updateArticleState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog()
        }

        is NetworkResult.Success -> {
            val message = stringResource(id = R.string.update_remote_success)
            LaunchedEffect(
                key1 = updateArticleState.value,
                block = {
                    onBack()
                    onShowSnackbar(message, null)
                }
            )
        }

        is NetworkResult.Error -> {
            val message = updateArticleState.value.msg ?: unknownErrorMsg
            LaunchedEffect(
                key1 = updateArticleState.value,
                block = {
                    onBack()
                    onShowSnackbar(message, null)
                }
            )
        }

        else -> {}
    }
    BackHandler { onBackAction() }

    val publishArticleState by editViewModel.publishArticleState.observeAsState()
    when (publishArticleState) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = "上传中...")
        }

        is NetworkResult.Success -> {
            onFinish()
            LaunchedEffect(
                key1 = publishArticleState,
                block = {
                    onShowSnackbar("上传成功", null)
                }
            )
        }

        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = publishArticleState,
                block = { onShowSnackbar("上传失败:${publishArticleState.asError()?.msg}", null) }
            )
        }

        else -> {}
    }

    val historyTags = editViewModel.getHistoryTags().myObserveAsState {
        editViewModel.updateTagsAfterGetHistoryTags(it)
    }

    var showBottomDrawer by remember { mutableStateOf(false) }
    AddArticleScreenContent(
        onBack = ::onBackAction,
        onPublish = { showBottomDrawer = true },
        editViewModel = editViewModel,
        onNavToWeb = onNavToWeb,
        onShowSnackbar = onShowSnackbar,
        onPublishedArticleDelete = onPublishedArticleDelete,
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (showBottomDrawer) {
        ModalBottomSheet(
            onDismissRequest = { showBottomDrawer = !showBottomDrawer },
            sheetState = sheetState,
            shape = RectangleShape
        ) {
            BottomSheetContent(
                reallyPublish = { onPublish() },
                editViewModel = editViewModel,
                historyTags = historyTags,
                onUpdateSuccess = { onUpdateSuccess(it, theArticle) },
                onShowSnackbar = onShowSnackbar,
                onDismissSheet = { showBottomDrawer = false }
            )
        }
    }
}

@Composable
private fun FillArticleEffect(
    article: Article?,
    editViewModel: EditViewModel,
    editType: EditType
) {
    LaunchedEffect(
        key1 = article,
        block = {
            editViewModel.fillArticle(article, editType)
        }
    )
}

private fun onUpdateSuccess(updated: Article, theArticle: PageItem<Article>?) {
    theArticle?.onDataChanged(updated)
}