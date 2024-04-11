package com.example.lunimary.ui.edit

import android.net.Uri
import com.example.lunimary.base.currentUser
import com.example.lunimary.model.Article
import com.example.lunimary.model.VisibleMode
import com.example.lunimary.model.source.local.Tag
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd


data class UiState(
    val title: String = empty,
    val body: String = empty,
    val tags: List<Tag> = emptyList(),
    val visibleMode: VisibleMode = VisibleMode.PUBLIC,
    val cover: String = empty,
    val uri: Uri = Uri.EMPTY,
    val editType: EditType = EditType.New,
    val theArticle: Article? = null,
    val isFillByArticle: Boolean = false,
) {
    val canSaveAsDraft: Boolean = title.isNotBlank() && body.isNotBlank()

    val canPublish: Boolean get() = (title.isNotBlank() && body.isNotBlank()) || theArticle?.link?.isNotBlank() ?: false

    fun updatedLocalArticle(): Article? {
        if (theArticle == null) return null
        return theArticle.copy(
            title = title,
            body = body,
            tags = tags.map { it.name }.toTypedArray(),
            visibleMode = visibleMode,
            cover = cover
        )
    }

    fun theArticleChanged(): Boolean = (theArticle?.let {
        it.title != title || it.body != body
                || tags.size != it.tags.size
                || !tags.all { t -> t.name in it.tags }
                || visibleMode != it.visibleMode
                || cover != it.cover
    } ?: false).also {
        "draftChanged:$it, tags=${tags}, fillTags=${theArticle?.tags.toString()}"
            .logd("live_test")
    }

    fun generateArticle(): Article {
        return when (editType) {
            EditType.New, EditType.Draft -> Article(
                title = title,
                body = body,
                userId = currentUser.id,
                username = currentUser.username,
                author = currentUser.username,
                visibleMode = visibleMode,
                tags = tags.map { it.name }.toTypedArray(),
                cover = cover
            )
            EditType.Edit -> theArticle!!.copy(
                title = title,
                body = body,
                visibleMode = visibleMode,
                tags = tags.map { it.name }.toTypedArray(),
                cover = cover
            )
        }
    }
}