package com.example.lunimary.ui.edit.edit

import android.widget.EditText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.base.notLogin
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.ui.common.FileViewModel
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.ui.edit.EditViewModel
import com.example.lunimary.util.unknownErrorMsg

@Composable
fun EditBottomOptions(
    onNavToWeb: () -> Unit,
    bodyEditText: EditText,
    fileViewModel: FileViewModel,
    onPreviewClick: () -> Unit,
    showAddLinkDialog: MutableState<Boolean>,
    viewModel: EditViewModel,
    onPublishedArticleDelete: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        ShowStudyMarkdown(onNavToWeb = onNavToWeb)
        val images = formatImages
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyRow {
            items(images.size) { index ->
                IconButton(
                    onClick = { bodyEditText.handleFormat(index, context) }
                ) {
                    Icon(imageVector = formatImages[index], contentDescription = null)
                }
            }
        }
        Box(contentAlignment = Alignment.Center) {
            EditOptions(
                fileViewModel = fileViewModel,
                showAddLinkDialog = showAddLinkDialog,
                viewModel = viewModel,
                onPublishedArticleDelete = onPublishedArticleDelete,
                onShowSnackbar = onShowSnackbar,
                onBack = onBack
            )
            LinearButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onClick = {
                    keyboardController?.hide()
                    onPreviewClick()
                },
                text = stringResource(id = R.string.preview)
            )
        }
    }
}

@Composable
private fun EditOptions(
    fileViewModel: FileViewModel,
    showAddLinkDialog: MutableState<Boolean>,
    viewModel: EditViewModel,
    onPublishedArticleDelete: () -> Unit,
    onBack: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
) {
    val context = LocalContext.current
    val editOptions = listOf(EditOption.Image, EditOption.AddLink, EditOption.Delete)
    val showDeleteDialog = remember { mutableStateOf(false) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 100.dp)
    ) {
        itemsIndexed(editOptions) { _, item ->
            IconButton(
                onClick = {
                    when (item) {
                        EditOption.Image -> {
                            if (context.isCurrentlyConnected()) {
                                if (!notLogin()) {
                                    fileViewModel.updateShowImageSelector(true)
                                } else {
                                    onShowSnackbar("当前没有登录，请登录后重试！", null)
                                }
                            } else {
                                onShowSnackbar("当前没有联网，请联网后重试！", null)
                            }
                        }

                        EditOption.AddLink -> {
                            showAddLinkDialog.value = true
                        }

                        EditOption.Delete -> {
                            if (viewModel.uiState.canSaveAsDraft) {
                                showDeleteDialog.value = true
                            }
                        }
                    }
                }
            ) { Icon(imageVector = item.icon, contentDescription = null) }
        }
    }

    LunimaryDialog(
        text = when (viewModel.uiState.editType) {
            EditType.Edit -> {
                stringResource(id = R.string.confirm_delete_published_article)
            }

            EditType.New -> {
                stringResource(id = R.string.confirm_delete_new_article)
            }

            EditType.Draft -> {
                stringResource(id = R.string.confirm_delete_draft)
            }
        },
        onConfirmClick = {
            when (viewModel.uiState.editType) {
                EditType.Edit -> {
                    viewModel.deletePublishedArticle()
                }

                EditType.New -> {
                    viewModel.saveAsDraftEnabled = false ; onBack()
                }

                EditType.Draft -> {
                    viewModel.deleteDraft() ; onBack()
                }
            }
        },
        openDialog = showDeleteDialog
    )
    val deleteState = viewModel.deleteState.collectAsStateWithLifecycle()
    when (deleteState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog()
        }

        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = deleteState.value,
                block = {
                    onShowSnackbar(deleteState.value.msg ?: unknownErrorMsg, null)
                }
            )
        }

        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = deleteState.value,
                block = {
                    if (viewModel.uiState.editType == EditType.Edit) {
                        deleteState.value.msg?.let { onShowSnackbar(it, null) }
                    } else {
                        onShowSnackbar("删除成功！", null)
                    }
                    onPublishedArticleDelete()
                }
            )
        }

        else -> {}
    }
}
