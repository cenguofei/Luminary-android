package com.example.lunimary.design

import android.text.util.Linkify
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import coil.ImageLoader
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.base.mmkv.SettingMMKV
import dev.jeziellago.compose.markdowntext.AutoSizeConfig
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun LunimaryMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    linkColor: Color = Color.Unspecified,
    truncateOnTextOverflow: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    isTextSelectable: Boolean = false,
    autoSizeConfig: AutoSizeConfig? = null,
    @FontRes fontResource: Int? = null,
    @IdRes viewId: Int? = null,
    disableLinkMovementMethod: Boolean = false,
    imageLoader: ImageLoader? = null,
    linkifyMask: Int = Linkify.EMAIL_ADDRESSES or Linkify.PHONE_NUMBERS or Linkify.WEB_URLS,
    style: TextStyle = TextStyle.Default,
    onLinkClicked: ((String) -> Unit)? = null,
    onTextLayout: ((numLines: Int) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val systemDarkMode = isSystemInDarkTheme()
    val theLinkColor = when (linkColor) {
        Color.Unspecified -> {
            when {
                SettingMMKV.userHasSetTheme -> {
                    if (SettingMMKV.darkThemeSetting == DarkThemeSetting.DarkMode) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Blue
                    }
                }

                systemDarkMode -> MaterialTheme.colorScheme.primary
                else -> Color.Blue
            }
        }
        else -> linkColor
    }
    val theTextStyle = if (style == TextStyle.Default) LocalTextStyle.current.copy(
        color = MaterialTheme.colorScheme.onSurface,
    ) else style
    MarkdownText(
        modifier = modifier,
        markdown = markdown,
        linkColor = theLinkColor,
        style = theTextStyle,
        truncateOnTextOverflow = truncateOnTextOverflow,
        maxLines = maxLines,
        isTextSelectable = isTextSelectable,
        autoSizeConfig = autoSizeConfig,
        fontResource = fontResource,
        onClick = onClick,
        disableLinkMovementMethod = disableLinkMovementMethod,
        imageLoader = imageLoader,
        linkifyMask = linkifyMask,
        onLinkClicked = onLinkClicked,
        onTextLayout = onTextLayout,
        viewId = viewId
    )
}