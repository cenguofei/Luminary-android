package com.example.lunimary.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lunimary.R
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.base.mmkv.SettingMMKV.darkThemeSetting
import com.example.lunimary.design.background.LunimaryBackground
import com.example.lunimary.design.components.LBHorizontalDivider
import com.example.lunimary.design.theme.LunimaryTheme

@Composable
fun SettingsItems(
    modifier: Modifier,
    darkThemeSettingState: State<DarkThemeSetting>,
    onThemeSettingChange: (DarkThemeSetting) -> Unit,
    onNavToPrivacy: () -> Unit,
    onNavToInfo: () -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            val showDarkModeChooser = remember { mutableStateOf(false) }
            val rotation =
                animateFloatAsState(targetValue = if (showDarkModeChooser.value) 90f else 0f)
            SettingItem(
                text = stringResource(id = R.string.dark_mode),
                endContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer { rotationZ = rotation.value }
                    )
                },
                icon = Icons.Default.Contrast,
                onClick = {
                    showDarkModeChooser.value = !showDarkModeChooser.value
                }
            )
            AnimatedVisibility(
                visible = showDarkModeChooser.value,
                enter = slideInVertically { -it / 5 },
                exit = shrinkOut(shrinkTowards = Alignment.TopCenter) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LBHorizontalDivider()
                    SettingItem(
                        text = stringResource(id = R.string.dark_mode),
                        icon = Icons.Default.DarkMode,
                        endContent = {
                            if (darkThemeSettingState.value == DarkThemeSetting.DarkMode) {
                                Text(
                                    text = stringResource(id = R.string.already_set),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        },
                        onClick = {
                            if (darkThemeSetting != DarkThemeSetting.DarkMode) {
                                onThemeSettingChange(DarkThemeSetting.DarkMode)
                            }
                        }
                    )
                    LBHorizontalDivider()
                    SettingItem(
                        text = stringResource(id = R.string.night_mode),
                        icon = Icons.Default.ModeNight,
                        endContent = {
                            if (darkThemeSettingState.value == DarkThemeSetting.NightMode) {
                                Text(
                                    text = stringResource(id = R.string.already_set),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        },
                        onClick = {
                            if (darkThemeSetting != DarkThemeSetting.NightMode) {
                                onThemeSettingChange(DarkThemeSetting.NightMode)
                            }
                        }
                    )
                    LBHorizontalDivider()
                    SettingItem(
                        text = stringResource(id = R.string.follow_system),
                        icon = Icons.Default.BrightnessMedium,
                        endContent = {
                            if (darkThemeSettingState.value == DarkThemeSetting.FollowSystem) {
                                Text(
                                    text = stringResource(id = R.string.already_set),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        },
                        onClick = {
                            if (darkThemeSetting != DarkThemeSetting.FollowSystem) {
                                onThemeSettingChange(DarkThemeSetting.FollowSystem)
                                darkThemeSetting = DarkThemeSetting.FollowSystem
                            }
                        }
                    )
                }
            }
            LBHorizontalDivider()
            SettingItem(
                text = stringResource(id = R.string.privacy_protocol),
                icon = Icons.Default.PrivacyTip,
                onClick = onNavToPrivacy
            )
            LBHorizontalDivider()
            SettingItem(
                text = stringResource(id = R.string.modify_individual_info),
                icon = Icons.Default.EditNote,
                onClick = onNavToInfo
            )
        }
    }
}

@Preview
@Composable
fun SettingsItemsPreview() {
    LunimaryTheme {
        LunimaryBackground {
//            SettingsItems(modifier = Modifier, darkThemeSettingState = darkThemeSettingState)
        }
    }
}