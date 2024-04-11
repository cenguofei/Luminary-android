package com.example.lunimary.ui.browse.compositions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lunimary.R
import com.example.lunimary.base.DataState
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.CascadeMenuItem
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.model.Article
import com.example.lunimary.model.VisibleMode
import com.example.lunimary.ui.browse.BrowseViewModel
import com.example.lunimary.ui.browse.UiState
import com.example.lunimary.ui.edit.EditType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ArticleOptions(
    uiState: UiState,
    browseViewModel: BrowseViewModel,
    navToEdit: (EditType, Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val shareArticle = remember { mutableStateOf(false) }
    val appName = stringResource(id = R.string.app_name)
    val showOptions = remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(
        key1 = shareArticle.value,
        block = {
            if (shareArticle.value) {
                val intent = ShareCompat.IntentBuilder(context)
                    .setType("text/plain")
                    .setText(
                        """
            欢迎来到Lunimary-Blog！ 使用Lunimary-Blog App搜索"${uiState.article.title}"，获取更多资讯内容。
        """.trimIndent()
                    )
                    .setChooserTitle(appName)
                    .createChooserIntent()
                context.startActivity(intent)
                shareArticle.value = !shareArticle.value
            }
        }
    )
    val showAlertDialog = remember { mutableStateOf(false) }
    LunimaryDialog(
        text = stringResource(id = R.string.confirm_delete_article),
        onConfirmClick = { browseViewModel.delete() },
        openDialog = showAlertDialog
    )
    fun onItemSelected(id: String) {
        showOptions.value = false
        when (id) {
            ArticleSettings.Share.id -> {
                shareArticle.value = true
            }

            ArticleSettings.CopyLink.id -> {
                browseViewModel.copyLink()
            }

            ArticleSettings.PermissionSetting.id -> {}
            ArticleSettings.Public.id -> {
                browseViewModel.updateVisibility(VisibleMode.PUBLIC)
            }

            ArticleSettings.Friend.id -> {
                browseViewModel.updateVisibility(VisibleMode.FRIEND)
            }

            ArticleSettings.OWN.id -> {
                browseViewModel.updateVisibility(VisibleMode.OWN)
            }

            ArticleSettings.Delete.id -> {
                showAlertDialog.value = true
            }

            ArticleSettings.Edit.id -> {
                navToEdit(EditType.Edit, uiState.article)
            }
        }
    }
    Box(contentAlignment = Alignment.TopEnd) {
        CascadeMenu(
            isOpen = showOptions.value,
            menu = buildMenu(
                isMyArticle = uiState.isMyArticle,
                currentVisibleMode = uiState.article.visibleMode
            ),
            onItemSelected = ::onItemSelected,
            onDismiss = {
                showOptions.value = !showOptions.value
            },
            offset = DpOffset(8.dp, 0.dp)
        )
        IconButton(onClick = { showOptions.value = !showOptions.value }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }
    when (val state = browseViewModel.updateArticleMessageState) {
        is DataState.Success -> {
            LaunchedEffect(
                key1 = state.message,
                block = {
                    onShowSnackbar(state.message, null)
                }
            )
        }

        is DataState.Failed -> {
            LaunchedEffect(
                key1 = state.message,
                block = {
                    onShowSnackbar(state.message, null)
                }
            )
        }

        is DataState.None -> {}
    }
}

enum class ArticleSettings(
    val id: String,
    val title: String,
    val icon: ImageVector
) {
    Share("share", "分享", Icons.Default.Share),
    CopyLink("copy_link", "复制连接", Icons.Default.Link),

    PermissionSetting("permission_setting", "权限设置", Icons.Default.ManageAccounts),
    Public("public", "所有人可见", Icons.Default.LockOpen),
    Friend("friend", "互关朋友可见", Icons.Default.Group),
    OWN("Oneself", "仅自己可见", Icons.Default.Lock),

    Delete("delete", "删除", Icons.Default.Delete),

    Edit("edit", "编辑", Icons.Default.Edit)
}

private fun buildMenu(
    isMyArticle: Boolean,
    currentVisibleMode: VisibleMode
): CascadeMenuItem<String> {
    return cascadeMenu {
        item(ArticleSettings.Share.id, ArticleSettings.Share.title) {
            icon(ArticleSettings.Share.icon)
        }
        item(ArticleSettings.CopyLink.id, ArticleSettings.CopyLink.title) {
            icon(ArticleSettings.CopyLink.icon)
        }
        if (!isMyArticle) {
            return@cascadeMenu
        }
        item(ArticleSettings.PermissionSetting.id, ArticleSettings.PermissionSetting.title) {
            icon(ArticleSettings.PermissionSetting.icon)

            val publicTitle = if (currentVisibleMode == VisibleMode.PUBLIC) {
                ArticleSettings.Public.title.plus("   已设置")
            } else ArticleSettings.Public.title
            item(ArticleSettings.Public.id, publicTitle) {
                icon(ArticleSettings.Public.icon)
            }
            val friendTitle = if (currentVisibleMode == VisibleMode.FRIEND) {
                ArticleSettings.Friend.title.plus("   已设置")
            } else {
                ArticleSettings.Friend.title
            }
            item(ArticleSettings.Friend.id, friendTitle) {
                icon(ArticleSettings.Friend.icon)
            }
            val ownTitle = if (currentVisibleMode == VisibleMode.OWN) {
                ArticleSettings.OWN.title.plus("   已设置")
            } else {
                ArticleSettings.OWN.title
            }
            item(ArticleSettings.OWN.id, ownTitle) {
                icon(ArticleSettings.OWN.icon)
            }
        }
        item(ArticleSettings.Delete.id, ArticleSettings.Delete.title) {
            icon(ArticleSettings.Delete.icon)
        }
        item(ArticleSettings.Edit.id, ArticleSettings.Edit.title) {
            icon(ArticleSettings.Edit.icon)
        }
    }
}
