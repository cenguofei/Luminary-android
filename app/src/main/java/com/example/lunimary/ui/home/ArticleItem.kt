package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.design.Tag
import com.example.lunimary.design.tagColors
import com.example.lunimary.models.Article
import com.example.lunimary.models.fileBaseUrl

data class ArticleItemContainerColor(
    val visitedColor: Color,
    val normalColor: Color
) {
    companion object {
        val Default: ArticleItemContainerColor
            @Composable get() = ArticleItemContainerColor(
                visitedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                normalColor = MaterialTheme.colorScheme.surface
            )
    }
}

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    onItemClick: (Article) -> Unit,
    article: Article,
    visited: Boolean = false,
    showOptionsIcon: Boolean = false,
    onOptionsClick: () -> Unit = {},
    containerColor: ArticleItemContainerColor = ArticleItemContainerColor.Default
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = { onItemClick(article) },
        color = if (visited) containerColor.visitedColor else containerColor.normalColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Title(text = article.title, modifier = Modifier.weight(1f))
                if (showOptionsIcon) {
                    IconButton(onClick = onOptionsClick) {
                        Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Content(content = article.body, modifier = Modifier.weight(0.6f))
                ArticlePicture(pic = article.cover, modifier = Modifier.weight(0.4f))
            }
            Spacer(modifier = Modifier.height(4.dp))
            val tagWithColor = remember { article.tags.map { it to tagColors.random() } }
            Labels(tagWithColor = tagWithColor)
            Spacer(modifier = Modifier.height(4.dp))
            AboutTheArticle(modifier = Modifier, article)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AboutTheArticle(
    modifier: Modifier = Modifier,
    article: Article
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.weight(1f)) {
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
        ArticleSingleAmount(text = "${article.daysFromToday}天前")
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
            Tag(tag = com.example.lunimary.models.source.local.Tag(name = item.first, color = item.second.toArgb()))
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
        text = content,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}