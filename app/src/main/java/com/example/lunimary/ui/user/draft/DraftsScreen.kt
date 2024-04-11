package com.example.lunimary.ui.user.draft

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.nicepage.LunimaryStateContent
import com.example.lunimary.model.Article
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.edit.EditType

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.draftsScreen(
    appState: LunimaryAppState
) {
    composable(
        route = Screens.Drafts.route,
    ) {
        DraftsScreen(
            onBack = { appState.popBackStack() },
            onEdit = { appState.navToEdit(theArticle = PageItem(it), editType = EditType.Draft) }
        )
    }
}

@Composable
fun DraftsScreen(
    onBack: () -> Unit,
    onEdit: (Article) -> Unit,
) {
    val draftsViewModel: DraftsViewModel = viewModel()
    val drafts = draftsViewModel.findDrafts().observeAsState()
    LunimaryStateContent {
        LunimaryToolbar(
            onBack = onBack,
            between = {
                Text(
                    text = stringResource(id = R.string.draft),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.statusBarsPadding()
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            drafts.value?.sortedBy { it.timestamp }?.let { articles ->
                items(articles.size) {
                    DraftItem(
                        onClick = { _ -> onEdit(articles[it]) },
                        onItemSelected = { operation, selectedArticle ->
                            when (operation) {
                                DraftItemOperations.Remove -> {
                                    draftsViewModel.remove(selectedArticle)
                                }
                            }
                        },
                        canOperate = true,
                        draftsNum = articles.size,
                        article = articles[it]
                    )
                }
            }
        }
    }
}