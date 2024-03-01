package com.example.lunimary.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.design.Tag
import com.example.lunimary.models.Article

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    visited: Boolean = false,
    article: Article
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = onItemClick,
        color = when {
            visited -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            else -> MaterialTheme.colorScheme.surface
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Title(text = article.title, modifier = Modifier)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Content(content = article.body, modifier = Modifier.weight(2f))
                ArticlePicture(pic = article.pictures.firstOrNull(), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Labels(article.tags.toList())
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
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    if (tags.isEmpty()) return
    LazyRow(modifier = modifier) {
        itemsIndexed(tags) { _, item ->
            Tag(name = item)
        }
    }
}

@Composable
fun ArticlePicture(
    modifier: Modifier = Modifier,
    pic: String?
) {
    if (pic == null) return
    Surface(modifier = modifier.height(90.dp)) {
        AsyncImage(
            model = pic,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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