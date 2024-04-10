package com.example.lunimary.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.Tag
import com.example.lunimary.design.tagColors
import com.example.lunimary.models.Article
import com.example.lunimary.models.fileBaseUrl

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    onItemClick: (PageItem<Article>) -> Unit,
    articlePageItem: PageItem<Article>,
    visited: Boolean = false,
    topEndOptions: (@Composable BoxScope.() -> Unit)? = null,
    containerColor: ArticleItemContainerColor = ArticleItemContainerColor.Default,
    showAboutArticle: Boolean = true
) {
    val article = articlePageItem.data
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onItemClick(articlePageItem) },
        color = if (visited) containerColor.visitedColor else containerColor.normalColor
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Title(text = article.title, modifier = Modifier)
                }
                if (topEndOptions != null) {
                    Box(
                        contentAlignment = Alignment.TopEnd,
                        content = topEndOptions,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                if (article.isLunimaryStation) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val contentModifier =
                            if (article.cover.isEmpty() || !article.cover.startsWith("res/uploads")) {
                                Modifier.weight(1f)
                            } else Modifier.weight(0.6f)
                        Content(content = article.body, modifier = contentModifier)
                        if (article.cover.isNotEmpty() && article.cover.startsWith("res/uploads")) {
                            ArticlePicture(pic = article.cover, modifier = Modifier.weight(0.4f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                val tagWithColor = remember { article.tags.map { it to tagColors.random() } }
                Labels(tagWithColor = tagWithColor)
                Spacer(modifier = Modifier.height(4.dp))
                if (!article.isLunimaryStation) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (article.userId == currentUser.id) "你转发" else "${article.username}转发",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (showAboutArticle) {
                    AboutTheArticle(modifier = Modifier, article)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutTheArticle(
    modifier: Modifier = Modifier,
    article: Article
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        FlowRow(modifier = modifier.weight(1f)) {
            ArticleSingleAmount(text = article.author)
            ArticleSingleAmount(
                text = "${article.viewsNum}阅读",
                modifier = Modifier.padding(start = 8.dp)
            )
            ArticleSingleAmount(
                text = "${article.comments}评论",
                modifier = Modifier.padding(start = 8.dp)
            )
            ArticleSingleAmount(
                text = "${article.likes}赞",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        ArticleSingleAmount(text = article.daysFromToday)
    }
}

@Composable
private fun ArticleSingleAmount(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
fun Labels(
    modifier: Modifier = Modifier,
    tagWithColor: List<Pair<String, Color>>
) {
    if (tagWithColor.isEmpty()) return
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(tagWithColor) { _, item ->
            Tag(
                tag = com.example.lunimary.models.source.local.Tag(
                    name = item.first,
                    color = item.second.toArgb()
                ),
                padding = PaddingValues(vertical = 3.dp, horizontal = 4.dp),
                style = TextStyle(fontSize = 8.sp)
            )
        }
    }
}

@Composable
fun ArticlePicture(
    modifier: Modifier = Modifier,
    height: Dp = 90.dp,
    pic: String?
) {
    if (pic == null) return
    Surface(modifier = modifier.height(height), shape = RoundedCornerShape(8)) {
        AsyncImage(
            model = fileBaseUrl + pic,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.cover)
        )
    }
}

@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Content(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = markdownToPlainText(content),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

fun markdownToPlainText(markdownText: String): String {
    return try {
        val boldPattern = "\\*\\*(.*?)\\*\\*".toRegex()
        val italicPattern = "_([^_]+)_".toRegex()
        val codePattern = "`([^`]+)`".toRegex()
        val linkPattern = "\\[([^\\]]+)\\]\\([^)]+\\)".toRegex()
        val headingPattern = "#+\\s*(.*)".toRegex()
        val quotePattern = ">\\s*(.*)".toRegex()
        val listPattern = "([\\-*+])\\s*(.*)".toRegex()

        val needLength = 3 * 25
        val markdown =
            if (markdownText.length <= needLength) markdownText else markdownText.substring(0, needLength + 1)

        var plainText = markdown

        plainText = boldPattern.replace(plainText, "$1")
        plainText = italicPattern.replace(plainText, "$1")
        plainText = codePattern.replace(plainText, "$1")
        plainText = linkPattern.replace(plainText, "$1")
        plainText = headingPattern.replace(plainText, "$1")
        plainText = quotePattern.replace(plainText, "$1")
        plainText = listPattern.replace(plainText, "$2")

        plainText
    } catch (e: Exception) {
        e.printStackTrace()
        markdownText
    }
}