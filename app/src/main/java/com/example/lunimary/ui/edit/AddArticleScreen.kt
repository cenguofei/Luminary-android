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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.design.LightAndDarkPreview
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.myObserveAsState
import com.example.lunimary.design.theme.LunimaryTheme
import com.example.lunimary.models.Article
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.ArticleNavArguments
import com.example.lunimary.ui.common.EDIT_DRAFT_ARTICLE_KEY
import com.example.lunimary.design.ChineseMarkdownWeb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.addArticleScreen(
    appState: LunimaryAppState,
    coroutineScope: CoroutineScope
) {
    composable(
        Screens.AddArticle.route,
    ) {
        var draftArticle = ArticleNavArguments[EDIT_DRAFT_ARTICLE_KEY]
        if ((draftArticle?.id ?: -1) < 0) {
            draftArticle = null
        }
        val editViewModel: EditViewModel = viewModel()
        val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
        val saveMessage = stringResource(id = R.string.auto_save_as_draft)
        val updateMessage = stringResource(id = R.string.updated_draft)
        val saveDraft = {
            if (editViewModel.isFillByArticle) {
                if (editViewModel.draftChanged()) {
                    coroutineScope.launch {
                        snackbarHostState?.showSnackbar(message = updateMessage)
                    }
                    editViewModel.updateDraft()
                }
            } else {
                if (editViewModel.anyNotEmpty()) {
                    editViewModel.saveAsDraft()
                    coroutineScope.launch {
                        snackbarHostState?.showSnackbar(message = saveMessage)
                    }
                }
            }
            appState.popBackStack()
        }
        BackHandler { saveDraft() }
        AddArticleScreen(
            onBack = { saveDraft() },
            onPublish = {
                editViewModel.publish(
                    isDraft = draftArticle != null,
                    draftArticle = draftArticle
                )
            },
            editViewModel = editViewModel,
            coroutineScope = coroutineScope,
            onFinish = { appState.navToUser(Screens.AddArticle.route) },
            draftArticle = draftArticle,
            onNavToWeb = { appState.navToWeb(ChineseMarkdownWeb) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArticleScreen(
    onBack: () -> Unit,
    onPublish: () -> Unit,
    editViewModel: EditViewModel,
    coroutineScope: CoroutineScope,
    onFinish: () -> Unit,
    draftArticle: Article?,
    onNavToWeb: () -> Unit
) {
    val addArticleState = editViewModel.addArticleState.observeAsState()
    editViewModel.fillDraftArticle(draftArticle)
    val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
    when (addArticleState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = "上传中...")
        }

        is NetworkResult.Success -> {
            onFinish()
            LaunchedEffect(
                key1 = addArticleState.value,
                block = {
                    coroutineScope.launch {
                        snackbarHostState?.showSnackbar(
                            message = "上传成功"
                        )
                    }
                }
            )
        }

        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = addArticleState.value,
                block = {
                    coroutineScope.launch {
                        snackbarHostState?.showSnackbar(
                            message = "上传失败:${addArticleState.value.asError()?.msg}"
                        )
                    }
                }
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
        coroutineScope = coroutineScope,
        onNavToWeb = onNavToWeb
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
                historyTags = historyTags
            )
        }
    }
}


@LightAndDarkPreview
@Composable
fun EditScreenPreview() {
    LunimaryTheme {
        LunimaryGradientBackground {
            AddArticleScreen(
                onBack = {},
                onPublish = {},
                editViewModel = viewModel(),
                coroutineScope = rememberCoroutineScope(),
                onFinish = {},
                draftArticle = null,
                {}
            )
        }
    }
}