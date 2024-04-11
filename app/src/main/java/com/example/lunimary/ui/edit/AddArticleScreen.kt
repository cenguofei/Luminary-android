package com.example.lunimary.ui.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.base.notLogin
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.ChineseMarkdownWeb
import com.example.lunimary.model.Article
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.ArticleNavArguments
import com.example.lunimary.ui.common.EDIT_ARTICLE_KEY
import com.example.lunimary.ui.common.EDIT_TYPE_KEY
import com.example.lunimary.util.logd
import kotlinx.coroutines.CoroutineScope

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

        AddArticleScreenRoute(
            editType = editType,
            theArticle = theArticle,
            onPublish = {
                if (!notLogin()) {
                    editViewModel.publish()
                } else {
                    onShowSnackbar("您当前为未登录状态，请登录后再进行文章发布！", null)
                }
            },
            editViewModel = editViewModel,
            onFinish = {
                appState.navToUser(if (theArticle == null) Screens.AddArticle.route else Screens.Drafts.route)
            },
            onNavToWeb = { appState.navToWeb(ChineseMarkdownWeb) },
            onShowSnackbar = onShowSnackbar,
            onPublishedArticleDelete = {
                "onPublishedArticleDelete, deleted=${theArticle?.deleted}, data=${theArticle?.data}".logd("delete_remote_article")
                theArticle?.onDeletedStateChange(true)
                appState.popBackStack()
            },
            coroutineScope = coroutineScope,
            onBack = appState::popBackStack
        )
    }
}