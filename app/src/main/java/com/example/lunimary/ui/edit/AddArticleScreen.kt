package com.example.lunimary.ui.edit

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.notLogin
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.ChineseMarkdownWeb
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.design.myObserveAsState
import com.example.lunimary.models.Article
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.ArticleNavArguments
import com.example.lunimary.ui.common.EDIT_ARTICLE_KEY
import com.example.lunimary.ui.common.EDIT_TYPE_KEY
import com.example.lunimary.ui.edit.bottomsheet.BottomSheetContent
import com.example.lunimary.util.logd
import com.example.lunimary.util.unknownErrorMsg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.addArticleScreen(
    appState: LunimaryAppState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(
        Screens.AddArticle.route,
    ) {
        var theArticle = ArticleNavArguments[EDIT_ARTICLE_KEY] as? PageItem<Article>
        val editType = ArticleNavArguments[EDIT_TYPE_KEY] as? EditType ?: EditType.New
        if ((theArticle?.data?.id ?: -1) < 0) {
            theArticle = null
        }
        val editViewModel: EditViewModel = viewModel()
        FillArticleEffect(article = theArticle?.data, editViewModel = editViewModel, editType = editType)
        val saveMessage = stringResource(id = R.string.auto_save_as_draft)
        val updateMessage = stringResource(id = R.string.has_updated_draft)
        val openDialog = remember { mutableStateOf(false) }
        val updateArticleState = editViewModel.updateArticleState.collectAsStateWithLifecycle()

        val onBackAction = {
            when (editType) {
                EditType.New -> {
                    if (editViewModel.uiState.value.canSaveAsDraft && editViewModel.shouldSaveAsDraft) {
                        if (editViewModel.saveAsDraftEnabled) {
                            editViewModel.saveAsDraft()
                            coroutineScope.launch {
                                onShowSnackbar(saveMessage, null)
                            }
                        }
                    }
                    appState.popBackStack()
                }

                EditType.Draft -> {
                    if (editViewModel.uiState.value.theArticleChanged() && editViewModel.shouldUpdateDraft) {
                        coroutineScope.launch {
                            onShowSnackbar(updateMessage, null)
                        }
                        editViewModel.updateDraft()
                    }
                    appState.popBackStack()
                }

                EditType.Edit -> {
                    if (editViewModel.uiState.value.theArticleChanged()) {
                        openDialog.value = true
                    } else {
                        appState.popBackStack()
                    }
                }
            }
        }
        LunimaryDialog(
            text = stringResource(id = R.string.update_of_remote_not_saved),
            openDialog = openDialog,
            onConfirmClick = { editViewModel.updateRemoteArticle { onUpdateSuccess(it, theArticle) } },
            onCancelClick = { appState.popBackStack() }
        )
        when(updateArticleState.value) {
            is NetworkResult.Loading -> {
                LoadingDialog()
            }
            is NetworkResult.Success -> {
                val message = stringResource(id = R.string.update_remote_success)
                LaunchedEffect(
                    key1 = updateArticleState.value,
                    block = {
                        appState.popBackStack()
                        coroutineScope.launch {
                            onShowSnackbar(message, null)
                        }
                    }
                )
            }
            is NetworkResult.Error -> {
                val message = updateArticleState.value.msg ?: unknownErrorMsg
                LaunchedEffect(
                    key1 = updateArticleState.value,
                    block = {
                        appState.popBackStack()
                        coroutineScope.launch {
                            onShowSnackbar(message, null)
                        }
                    }
                )
            }
            else -> { }
        }
        BackHandler { onBackAction() }
        AddArticleScreen(
            onBack = { onBackAction() },
            onPublish = {
                if (!notLogin()) {
                    editViewModel.publish()
                } else {
                    onShowSnackbar("您当前为未登录状态，请登录后再进行文章发布！", null)
                }
            },
            editViewModel = editViewModel,
            coroutineScope = coroutineScope,
            onFinish = {
                appState.navToUser(if (theArticle == null) Screens.AddArticle.route else Screens.Drafts.route)
            },
            onNavToWeb = { appState.navToWeb(ChineseMarkdownWeb) },
            onUpdateSuccess = { onUpdateSuccess(it, theArticle) },
            onShowSnackbar = onShowSnackbar,
            onPublishedArticleDelete = {
                "onPublishedArticleDelete, deleted=${theArticle?.deleted}, data=${theArticle?.data}".logd("delete_remote_article")
                theArticle?.onDeletedStateChange(true)
                appState.popBackStack()
            }
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArticleScreen(
    onBack: () -> Unit,
    onPublish: () -> Unit,
    editViewModel: EditViewModel,
    coroutineScope: CoroutineScope,
    onFinish: () -> Unit,
    onNavToWeb: () -> Unit,
    onUpdateSuccess: (updated: Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onPublishedArticleDelete: () -> Unit
) {
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
        onBack = onBack,
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
                coroutineScope = coroutineScope,
                historyTags = historyTags,
                onUpdateSuccess = onUpdateSuccess,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

private fun onUpdateSuccess(updated: Article, theArticle: PageItem<Article>?) {
    theArticle?.onDataChanged(updated)
}